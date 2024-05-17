package roomescape.member.dto;

import roomescape.member.domain.Role;

public record LoginMemberInToken(
        Role role,
        String name,
        String email
) {
}
