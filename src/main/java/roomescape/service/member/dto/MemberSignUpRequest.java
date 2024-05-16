package roomescape.service.member.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.member.Member;

public record MemberSignUpRequest(
        @NotBlank(message = "이름은 공백일 수 없습니다.")
        String name,

        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        String email,

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        String password
) {
    public Member toMember() {
        return new Member(name, email, password);
    }
}
