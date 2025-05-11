package roomescape.auth.login.infrastructure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.login.infrastructure.token.JwtTokenManager;
import roomescape.auth.login.infrastructure.token.TokenExtractor;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {

    public static final String ADMIN_STRING = "ADMIN";

    private final JwtTokenManager jwtTokenManager;

    public AdminRoleInterceptor(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = TokenExtractor.extract(request);

        String role = jwtTokenManager.getRole(token);
        validateRoleIsAdmin(role);

        return true;
    }

    private static void validateRoleIsAdmin(final String role) {
        if (!role.equals(ADMIN_STRING)) {
            throw new ForbiddenException("관리자가 아닙니다.");
        }
    }
}
