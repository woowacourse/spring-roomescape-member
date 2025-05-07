package roomescape.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
