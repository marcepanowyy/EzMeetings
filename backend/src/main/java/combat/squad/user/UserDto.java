package combat.squad.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record UserDto(

        @NotBlank(message = "Email cannot be blank")
        @javax.validation.constraints.Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern.List({
                @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter"),
                @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit"),
                @Pattern(regexp = ".*[!@#$%^&*()].*", message = "Password must contain at least one special character")
        })
        String password
) {
}
