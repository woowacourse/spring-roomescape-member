package roomescape.controller.api.member.dto;

import roomescape.model.Role;

public record LoginMemberInfo(Long id, String name, String email, Role role) {

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }
}
