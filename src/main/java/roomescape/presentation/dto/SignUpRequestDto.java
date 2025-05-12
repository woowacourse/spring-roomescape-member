package roomescape.presentation.dto;

import jakarta.validation.constraints.NotEmpty;

public record SignUpRequestDto(
        @NotEmpty String email,
        @NotEmpty String password,
        @NotEmpty String name
) {
}
