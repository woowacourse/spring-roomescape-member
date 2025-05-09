package roomescape.member.controller.request;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String name
) {
}
