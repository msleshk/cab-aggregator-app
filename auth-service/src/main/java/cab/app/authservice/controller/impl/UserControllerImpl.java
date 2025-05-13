package cab.app.authservice.controller.impl;

import cab.app.authservice.controller.UserController;
import cab.app.authservice.dto.request.RefreshTokenDto;
import cab.app.authservice.dto.request.SignInDto;
import cab.app.authservice.dto.request.SignUpDto;
import cab.app.authservice.dto.response.UserResponse;
import cab.app.authservice.dto.response.UserResponseTokenDto;
import cab.app.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/sign-in")
    public UserResponseTokenDto signIn(@Valid @RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

    @Override
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return userService.signUp(signUpDto);
    }

    @Override
    @PostMapping("/refresh-token")
    public UserResponseTokenDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return userService.refreshToken(refreshTokenDto);
    }
}
