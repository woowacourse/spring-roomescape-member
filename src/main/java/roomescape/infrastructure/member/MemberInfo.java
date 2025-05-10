package roomescape.infrastructure.member;

import roomescape.domain.member.Role;

public record MemberInfo(Long id, String name, String email, Role role) {

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }
}
