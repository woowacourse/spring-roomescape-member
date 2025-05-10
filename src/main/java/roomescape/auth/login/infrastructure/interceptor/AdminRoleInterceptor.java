package roomescape.auth.login.infrastructure.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.login.infrastructure.token.JwtTokenManager;

public class AdminRoleInterceptor implements HandlerInterceptor {

    public static final String ADMIN_STRING = "ADMIN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        validateCookieIsNonNull(cookies);

        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new UnauthorizedException("토큰을 찾을 수 없습니다."));

        String role = JwtTokenManager.getRole(tokenCookie.getValue());
        validateRoleIsAdmin(role);

        return true;
    }

    private static void validateRoleIsAdmin(final String role) {
        if (!role.equals(ADMIN_STRING)) {
            throw new ForbiddenException("관리자가 아닙니다.");
        }
    }

    private static void validateCookieIsNonNull(final Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("쿠키가 비었습니다.");
        }
    }
}
