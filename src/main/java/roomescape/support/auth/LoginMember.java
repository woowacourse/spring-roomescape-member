package roomescape.support.auth;

import roomescape.domain.MemberRole;

public record LoginMember(
        Long id,
        String name,
        String email,
        MemberRole memberRole
) {
}
