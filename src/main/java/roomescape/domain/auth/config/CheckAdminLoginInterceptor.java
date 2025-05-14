package roomescape.domain.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.service.JwtManager;

public class CheckAdminLoginInterceptor implements HandlerInterceptor {

    private final JwtManager jwtManager;
    private final String cookieKey;

    public CheckAdminLoginInterceptor(final JwtManager jwtManager, final JwtProperties jwtProperties) {
        this.jwtManager = jwtManager;
        this.cookieKey = jwtProperties.getCookieKey();
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return setForbiddenResponse(response);
        }

        final boolean isAdmin = isAdmin(cookies);
        if (isAdmin) {
            return true;
        }

        return setForbiddenResponse(response);
    }

    private boolean isAdmin(final Cookie[] cookies) {
        return findToken(cookies).map(jwtManager::getRole)
                .anyMatch(Roles::isAdmin);
    }

    private boolean setForbiddenResponse(final HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());

        return false;
    }

    private Stream<String> findToken(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookieKey.equals(cookie.getName()))
                .map(Cookie::getValue);
    }
}
