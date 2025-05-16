package roomescape.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.entity.Role;
import roomescape.global.exception.forbidden.ForbiddenException;
import roomescape.global.infrastructure.AuthTokenCookieProvider;
import roomescape.global.infrastructure.JwtTokenProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenCookieProvider authTokenCookieProvider;

    public CheckAdminInterceptor(JwtTokenProvider jwtTokenProvider, AuthTokenCookieProvider authTokenCookieProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authTokenCookieProvider = authTokenCookieProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authTokenCookieProvider.extractToken(request);
        Role role = jwtTokenProvider.getRole(token);
        if (role == Role.ADMIN) {
            return true;
        }
        throw new ForbiddenException("접근 권한이 없습니다.");
    }
}
