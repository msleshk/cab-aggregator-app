package cab.app.authservice.controller;

import cab.app.authservice.dto.request.RefreshTokenDto;
import cab.app.authservice.dto.request.SignInDto;
import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.UserResponse;
import cab.app.authservice.dto.response.UserResponseTokenDto;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface UserController {

    UserResponseTokenDto signIn(@Valid @RequestBody SignInDto signInDto);

    UserResponse signUp(@Valid @RequestBody SignUpDto signUpDto);

    UserResponseTokenDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto);
}