package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.auth.sign.exception.AuthException;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserRole;

import java.util.List;

public class ForbiddenException extends AuthException {

    public ForbiddenException(final UserId id,
                              final UserRole role,
                              final List<UserRole> requiredRole) {
        super(buildLogMessage(id, role, requiredRole),
                buildUserMessage());
    }

    private static String buildLogMessage(final UserId id,
                                          final UserRole role,
                                          final List<UserRole> requiredRoles) {
        final String requiredRolesStr = String.join(", ",
                requiredRoles.stream()
                        .map(UserRole::name)
                        .toList());

        return String.format(
                "Forbidden access: user(id=%s, role=%s) tried to access resource requiring roles=[%s]",
                id.getValue(), role.name(), requiredRolesStr);
    }

    private static String buildUserMessage() {
        return "권한이 존재하지 않습니다";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
