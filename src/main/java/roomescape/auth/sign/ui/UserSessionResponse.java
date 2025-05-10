package roomescape.auth.sign.ui;

import roomescape.auth.resolver.UserSession;

public record UserSessionResponse(Long id,
                                  String name,
                                  String role) {

    public static UserSessionResponse from(final UserSession session) {
        return new UserSessionResponse(
                session.id().getValue(),
                session.name().getValue(),
                session.role().name()
        );
    }
}
