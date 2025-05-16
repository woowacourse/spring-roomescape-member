package roomescape.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignupRequest(
        @NotNull
        @Email
        String email,
        @NotBlank
        String name,
        @NotBlank
        String password
) {
}
