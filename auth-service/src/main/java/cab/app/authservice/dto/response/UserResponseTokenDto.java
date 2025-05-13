package cab.app.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseTokenDto(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("refresh_expires_in")
        Integer refreshExpiresIn,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("not_before_policy")
        Integer notBeforePolicy,

        @JsonProperty("session_state")
        String sessionState,

        String scope
) {
}
