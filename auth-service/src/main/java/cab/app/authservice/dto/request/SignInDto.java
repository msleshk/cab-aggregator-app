package cab.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.app.authservice.util.RegExp.EMAIL_PATTERN;

public record SignInDto(
        @NotNull(message = "{email.notnull}")
        @Length(max = 255, message = "{email.length}")
        @Pattern(regexp = EMAIL_PATTERN, message = "{email.pattern}")
        String email,

        @NotBlank(message = "{password.empty}")
        @Length(max = 255, message = "{email.length}")
        String password
) {
}
