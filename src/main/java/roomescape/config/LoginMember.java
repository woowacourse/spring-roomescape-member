package roomescape.config;

import roomescape.business.domain.member.MemberRole;

public record LoginMember(
        Long id,
        String name,
        String email,
        MemberRole role
) {
}
