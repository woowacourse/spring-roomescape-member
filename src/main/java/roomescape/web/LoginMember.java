package roomescape.web;

import roomescape.entity.MemberRole;

public record LoginMember(
        Long id,
        String name,
        MemberRole role
) {
}
