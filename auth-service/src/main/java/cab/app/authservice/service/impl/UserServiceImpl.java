package cab.app.authservice.service.impl;

import cab.app.authservice.dto.request.RefreshTokenDto;
import cab.app.authservice.dto.request.SignInDto;
import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.UserResponse;
import cab.app.authservice.dto.response.UserResponseTokenDto;
import cab.app.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse signUp(SignUpDto signUpDto) {
        return null;
    }

    @Override
    public UserResponseTokenDto signIn(SignInDto signInDto) {
        return null;
    }

    @Override
    public UserResponseTokenDto refreshToken(RefreshTokenDto refreshTokenDto) {
        return null;
    }
}
