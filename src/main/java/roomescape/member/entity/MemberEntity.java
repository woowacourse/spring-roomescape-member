package roomescape.member.entity;

import roomescape.auth.domain.Role;
import roomescape.member.domain.Member;

public record MemberEntity(
        Long id,
        String name,
        String email,
        String password,
        Role role
) {
    public Member toMember() {
        return Member.of(id, name, email, password, role);
    }
}
