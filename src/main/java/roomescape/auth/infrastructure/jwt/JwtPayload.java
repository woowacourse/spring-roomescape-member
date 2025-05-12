package roomescape.auth.infrastructure.jwt;

import roomescape.auth.infrastructure.Role;

public record JwtPayload(
    String identifier,
    Role role
) {

}
