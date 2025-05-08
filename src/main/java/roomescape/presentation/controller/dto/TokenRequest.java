package roomescape.presentation.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenRequest(

        @NotNull
        @NotBlank
        String email,

        @NotNull
        @NotBlank
        String password
) {
}
