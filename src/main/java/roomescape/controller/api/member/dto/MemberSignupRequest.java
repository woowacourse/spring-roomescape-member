package roomescape.controller.api.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.model.Member;
import roomescape.model.Role;

public record MemberSignupRequest(

        @Email(message = "이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String name
) {

    public Member toEntity() {
        return new Member(name, email, password, Role.USER);
    }
}
