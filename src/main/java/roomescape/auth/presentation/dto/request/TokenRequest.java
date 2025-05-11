package roomescape.auth.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record TokenRequest(

        @NotNull
        String email,

        @NotNull
        String password
) {
}
