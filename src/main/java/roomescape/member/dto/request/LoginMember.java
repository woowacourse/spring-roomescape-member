package roomescape.member.dto.request;

import roomescape.member.entity.RoleType;

public record LoginMember(
        Long id,
        String name,
        RoleType role
) {
}
