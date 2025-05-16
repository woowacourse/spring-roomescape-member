package roomescape.auth.infrastructure;

import roomescape.member.domain.enums.Role;

public record JwtPayload(Long memberId, String name, String email, Role role) {
}
