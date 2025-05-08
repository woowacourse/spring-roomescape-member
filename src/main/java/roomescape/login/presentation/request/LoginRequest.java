package roomescape.login.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @Email String email,
        @NotNull String password
) {
}
