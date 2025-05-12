package cab.app.authservice.service.impl;

import cab.app.authservice.client.driver.DriverClientContainer;
import cab.app.authservice.client.passenger.PassengerClientContainer;
import cab.app.authservice.config.KeycloakProperties;
import cab.app.authservice.dto.request.RefreshTokenDto;
import cab.app.authservice.dto.request.SignInDto;
import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.UserResponse;
import cab.app.authservice.dto.response.UserResponseTokenDto;
import cab.app.authservice.exception.CreateUserException;
import cab.app.authservice.exception.KeycloakException;
import cab.app.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static cab.app.authservice.util.Constants.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final DriverClientContainer driverClientContainer;
    private final PassengerClientContainer passengerClientContainer;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    private static final String AUTH_TOKEN = "Bearer ";

    @Override
    public UserResponse signUp(SignUpDto signUpDto) {
        RealmResource realm = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realm.users();
        UserRepresentation userRep = buildUserRepresentation(signUpDto);

        Response response = createUserInKeycloak(usersResource, userRep);
        String createdUserId = CreatedResponseUtil.getCreatedId(response);

        String adminToken = keycloak.tokenManager().getAccessTokenString();

        try {
            UserResponse userResponse = createUserInDomain(signUpDto, adminToken);

            assignUserRole(realm, usersResource, createdUserId, signUpDto.role());

            return userResponse;

        } catch (Exception ex) {
            usersResource.delete(createdUserId);
            throw ex;
        }
    }

    private UserRepresentation buildUserRepresentation(SignUpDto dto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(dto.email());
        user.setEmail(dto.email());
        user.setFirstName(dto.name());
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.password());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", List.of(dto.phoneNumber()));
        attributes.put("gender", List.of(dto.gender()));
        user.setAttributes(attributes);

        return user;
    }

    private Response createUserInKeycloak(UsersResource users, UserRepresentation userRep) {
        Response response = users.create(userRep);
        if (response == null) {
            throw new ServiceUnavailableException();
        }

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            throw new CreateUserException(readResponseBody(response), response.getStatus());
        }

        return response;
    }

    private UserResponse createUserInDomain(SignUpDto dto, String token) {
        return switch (dto.role()) {
            case "PASSENGER" -> passengerClientContainer.createPassenger(dto, AUTH_TOKEN + token);
            case "DRIVER" -> driverClientContainer.createDriver(dto, AUTH_TOKEN + token);
            default -> throw new IllegalArgumentException("Unsupported role: " + dto.role());
        };
    }

    private void assignUserRole(RealmResource realm, UsersResource users, String userId, String roleName) {
        RoleRepresentation role = realm.roles().get(roleName).toRepresentation();
        users.get(userId).roles().realmLevel().add(List.of(role));
    }

    private String readResponseBody(Response response) {
        try (Scanner s = new Scanner(response.readEntity(InputStream.class)).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }

    @Override
    @SneakyThrows
    public UserResponseTokenDto signIn(SignInDto signInDto) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = buildTokenRequestEntity(
                signInDto.email(),
                signInDto.password(),
                GRANT_TYPE_PASSWORD_FIELD
        );

        ResponseEntity<String> response = sendTokenRequest(requestEntity);
        validateResponse(response);

        return objectMapper.readValue(response.getBody(), UserResponseTokenDto.class);
    }

    @Override
    @SneakyThrows
    public UserResponseTokenDto refreshToken(RefreshTokenDto refreshTokenDto) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_FIELD, REFRESH_TOKEN_FIELD);
        body.add(CLIENT_ID_FIELD, keycloakProperties.getUserManagement().getClientId());
        body.add(CLIENT_SECRET_FIELD, keycloakProperties.getUserManagement().getClientSecret());
        body.add(REFRESH_TOKEN_FIELD, refreshTokenDto.refreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = sendTokenRequest(requestEntity);
        validateResponse(response);

        return objectMapper.readValue(response.getBody(), UserResponseTokenDto.class);
    }

    private HttpEntity<MultiValueMap<String, String>> buildTokenRequestEntity(String username, String password, String grantType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE_FIELD, grantType);
        body.add(CLIENT_ID_FIELD, keycloakProperties.getUserManagement().getClientId());
        body.add(CLIENT_SECRET_FIELD, keycloakProperties.getUserManagement().getClientSecret());

        if (GRANT_TYPE_PASSWORD_FIELD.equals(grantType)) {
            body.add(USERNAME_FIELD, username);
            body.add(PASSWORD_FIELD, password);
        }

        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<String> sendTokenRequest(HttpEntity<MultiValueMap<String, String>> requestEntity) {
        String url = keycloakProperties.getUserManagement().getServerUrl()
                + "/realms/" + keycloakProperties.getRealm()
                + "/protocol/openid-connect/token";

        try {
            return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            throw new KeycloakException(e.getResponseBodyAsString(), e.getStatusCode().value());
        }
    }

    private void validateResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new KeycloakException(response.getBody(), response.getStatusCode().value());
        }
    }


}
