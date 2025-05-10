package roomescape.global.auth;

import java.lang.reflect.Method;
import roomescape.member.domain.Role;

public class AuthChecker {
    public static boolean checkAuthorization(Method method, Role currentUserRole) {
        if (method.isAnnotationPresent(Auth.class)) {
            if (currentUserRole == Role.ADMIN) {
                return true;
            }

            Auth auth = method.getAnnotation(Auth.class);
            Role requiredRole = auth.value();
            return currentUserRole == requiredRole;
        }
        return true;
    }
}

