package roomescape.auth.infrastructure;

import roomescape.auth.infrastructure.jwt.JwtPayload;

public record AuthorizationContext(
    String identifier,
    Role role
) {

    public static final String ATTRIBUTE_NAME = "principal";

    public static AuthorizationContext fromJwtPayload(JwtPayload jwtPayload) {
        return new AuthorizationContext(
            jwtPayload.identifier(),
            jwtPayload.role()
        );
    }
}
