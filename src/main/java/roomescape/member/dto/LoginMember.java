package roomescape.member.dto;

import roomescape.auth.domain.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
}
