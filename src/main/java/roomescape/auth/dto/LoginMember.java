package roomescape.auth.dto;

import roomescape.member.entity.Role;

public record LoginMember(Long id, Role role) {

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
