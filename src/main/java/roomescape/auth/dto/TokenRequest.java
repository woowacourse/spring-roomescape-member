package roomescape.auth.dto;

import jakarta.validation.constraints.NotNull;

public record TokenRequest(

        @NotNull
        String email,

        @NotNull
        String password
) {
}
