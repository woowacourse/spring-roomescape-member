package roomescape.presentation.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberRequestDto(
        @NotBlank(message = "이메일은 필수입니다.") String email,
        @NotBlank(message = "비밀번호는 필수입니다.") String password,
        @NotBlank(message = "사용자명은 필수입니다.") String name
) {
}
