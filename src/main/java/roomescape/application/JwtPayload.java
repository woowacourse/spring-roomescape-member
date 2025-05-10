package roomescape.application;

import roomescape.domain.Role;

public record JwtPayload(Long memberId, String name, Role role) {
}
