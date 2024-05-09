package roomescape.member.dto.request;

import roomescape.member.domain.Member;

import static roomescape.member.domain.Role.USER;

public record MemberJoinRequest(
        String email,
        String password,
        String name
) {

    public Member toModel() {
        return new Member(name, email, password, USER);
    }
}
