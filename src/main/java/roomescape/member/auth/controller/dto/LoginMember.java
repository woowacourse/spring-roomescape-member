package roomescape.member.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginMember(
        @NotBlank Long id,
        @NotBlank String email,
        @NotBlank String name,
        @NotBlank String role
) {
}
