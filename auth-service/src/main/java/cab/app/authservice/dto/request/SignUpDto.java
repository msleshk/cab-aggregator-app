package cab.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import static cab.app.authservice.util.RegExp.*;

public record SignUpDto(

        @NotNull(message = "{name.notnull}")
        @Length(max = 255, message = "{name.length}")
        String name,

        @NotNull(message = "{email.notnull}")
        @Length(max = 255, message = "{email.length}")
        @Pattern(regexp = EMAIL_PATTERN, message = "{email.pattern}")
        String email,

        @NotNull(message = "{phoneNumber.notnull}")
        @Length(max = 255, message = "{phoneNumber.length}")
        @Pattern(regexp = PHONE_NUMBER_PATTERN, message = "{phoneNumber.pattern}")
        String phoneNumber,

        @NotNull(message = "{gender.notnull}")
        @Length(max = 255, message = "{gender.length}")
        @Pattern(regexp = GENDER_PATTERN, message = "{gender.pattern}")
        String gender,

        @NotNull(message = "{role.notnull}")
        @Pattern(regexp = ROLE_PATTERN, message = "{role.pattern}")
        String role,

        @NotBlank(message = "{password.empty}")
        @Length(max = 255, message = "{password.length}")
        String password
) {
}
