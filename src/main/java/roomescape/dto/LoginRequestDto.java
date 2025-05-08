package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank String password,
        @NotBlank String email
) {
}
