package roomescape.global.auth;

import roomescape.member.entity.RoleType;

public record LoginMember(
        Long id,
        String name,
        RoleType role
) {
}
