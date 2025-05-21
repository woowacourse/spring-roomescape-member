package roomescape.dto.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.domain.member.Member;

public record MemberRegisterRequest(

        @NotBlank(message = "이름은 필수로 입력해야 합니다")
        String name,

        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password

) {
    public Member toEntity() {
        return Member.fromWithoutId(name, email, password);
    }
}
