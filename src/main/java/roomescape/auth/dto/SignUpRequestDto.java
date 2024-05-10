package roomescape.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import roomescape.member.domain.Member;

public record SignUpRequestDto(
        @Email(message = "이메일 형식으로 입력해야 합니다.(ex: email@email.com)") String email,
        @NotBlank(message = "비밀번호를 입력해야 합니다.") String password,
        @NotBlank(message = "사용자 이름을 입력해야 합니다.") String name
) {
    public Member toMember() {
        return new Member(email, password, name);
    }
}
