package roomescape.dto.member;

import static roomescape.domain.member.Role.USER;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberPassword;
import roomescape.domain.member.Role;

public record MemberCreateRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,

        @NotBlank(message = "이름을 입력해주세요.")
        String name) {

    public static MemberCreateRequest from(String email, String password, String name) {
        return new MemberCreateRequest(email, password, name);
    }

    public Member toDomain() {
        return new Member(
                null,
                new MemberName(name),
                new MemberEmail(email),
                new MemberPassword(password),
                USER
        );
    }
}
