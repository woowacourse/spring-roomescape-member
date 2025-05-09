package roomescape.dto.member;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String password,
        @NotBlank String email
) {
}
