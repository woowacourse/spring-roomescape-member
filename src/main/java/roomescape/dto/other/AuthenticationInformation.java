package roomescape.dto.other;

import roomescape.domain.MemberRole;

public record AuthenticationInformation(
        Long id,
        String name,
        MemberRole role
) {

    public boolean isAdmin() {
        return role == MemberRole.ADMIN;
    }
}
