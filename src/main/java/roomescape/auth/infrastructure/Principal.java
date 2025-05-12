package roomescape.auth.infrastructure;

import roomescape.auth.infrastructure.jwt.JwtPayload;

public record Principal(
    String identifier,
    Role role
) {

    public static Principal fromJwtPayload(JwtPayload jwtPayload) {
        return new Principal(
            jwtPayload.identifier(),
            jwtPayload.role()
        );
    }
}
