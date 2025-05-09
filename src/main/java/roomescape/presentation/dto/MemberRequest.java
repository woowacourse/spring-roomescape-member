package roomescape.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRequest(
        @NotBlank(message = "이름을 입력해주세요.")
        @Size(min = 1, max = 5, message = "사용자명은 최소 1글자, 최대 5글자여야합니다.")
        String name,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바르지 않은 이메일 형식입니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {
}
