package roomescape.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberSignUpRequest(
        @NotBlank(message = "이름은 빈 값일 수 없습니다")
        String name,

        @NotBlank(message = "이메일은 빈 값일 수 없습니다")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "패스워드는 빈 값일 수 없습니다")
        String password) {
}
