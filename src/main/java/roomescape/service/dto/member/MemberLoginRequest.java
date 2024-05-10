package roomescape.service.dto.member;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
