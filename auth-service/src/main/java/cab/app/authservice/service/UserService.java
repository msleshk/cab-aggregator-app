package cab.app.authservice.service;

import cab.app.authservice.dto.request.RefreshTokenDto;
import cab.app.authservice.dto.request.SignInDto;
import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.UserResponse;
import cab.app.authservice.dto.response.UserResponseTokenDto;

public interface UserService {

    UserResponse signUp(SignUpDto signUpDto);

    UserResponseTokenDto signIn(SignInDto signInDto);

    UserResponseTokenDto refreshToken(RefreshTokenDto refreshTokenDto);
}
