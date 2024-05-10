package roomescape.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email) {

    public static MemberLoginRequest of(String password, String email) {
        return new MemberLoginRequest(password, email);
    }
}
