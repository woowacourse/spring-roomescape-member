package roomescape.auth.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.member.domain.Member;

public record SignupRequest(
        @NotBlank(message = "이메일은 비어있을 수 없습니다.") String email,
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")String password,
        @NotBlank(message = "이름은 비어있을 수 없습니다.") String name
) {

    public Member toMember() {
        return new Member(name, email, password);
    }
}
