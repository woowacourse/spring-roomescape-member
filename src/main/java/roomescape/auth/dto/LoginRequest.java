package roomescape.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty String name,
        @NotEmpty @Email String email,
        @NotEmpty String password
) {
}
