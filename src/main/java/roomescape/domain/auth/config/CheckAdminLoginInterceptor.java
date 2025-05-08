package roomescape.domain.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.service.JwtManager;

@Slf4j
@Component
public class CheckAdminLoginInterceptor implements HandlerInterceptor {

    private static final String TOKEN_NAME = "token";

    private final JwtManager jwtManager;

    public CheckAdminLoginInterceptor(final JwtManager jwtManager) {
        this.jwtManager = jwtManager;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            final String token = findToken(cookies);
            if (token != null) {
                final Roles role = jwtManager.getRole(token);
                if (isAdmin(role)) {
                    return true;
                }
            }
        }

        setForbiddenResponse(response);
        return false;
    }

    private String findToken(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private boolean isAdmin(final Roles role) {
        return role != null && !role.isNotAdmin();
    }

    private void setForbiddenResponse(final HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
