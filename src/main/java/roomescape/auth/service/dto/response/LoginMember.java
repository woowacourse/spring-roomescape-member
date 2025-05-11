package roomescape.auth.service.dto.response;

import roomescape.auth.entity.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public boolean hasRole(Role role) {
        return this.role == role;
    }
}
