package roomescape.global.auth;

import roomescape.member.domain.Role;

public record AuthUser(
        Long id,
        String name,
        Role role
) {

    public boolean isAdmin() {
        return role.isAdmin();
    }
}
