package roomescape.member.dto.request;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public record MemberJoinRequest(
        String email,
        String password,
        String name
) {

    public Member toModel(Role role) {
        return new Member(name, email, password, role);
    }
}
