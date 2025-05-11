package roomescape.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
