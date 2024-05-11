package roomescape.member.service.dto;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
) {
}
