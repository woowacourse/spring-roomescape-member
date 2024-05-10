package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserLoginRequest(
        @NotNull
        String email,
        @NotNull
        String password
) {
}
