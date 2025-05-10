package roomescape.presentation.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(
        @NotEmpty String email,
        @NotEmpty String password) {
}
