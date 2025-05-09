package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.Member;

public record MemberRegisterRequest(

        @NotBlank(message = "이름은 필수로 입력해야 합니다")
        String name,

        @NotBlank(message = "이메일은 필수 입력입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password

) {
    public Member toEntity() {
        return Member.fromWithoutId(this.name, this.email, this.password);
    }
}
