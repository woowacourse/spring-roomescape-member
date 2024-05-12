package roomescape.config.Interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.JwtTokenProvider;
import roomescape.domain.Role;
import roomescape.web.exception.AuthorizationException;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String tokenValue = tokenProvider.getTokenValue(cookies);
        Role role = Role.valueOf(tokenProvider.getTokenRole(tokenValue));

        if (!role.isAdmin()) {
            throw new AuthorizationException("권한이 없습니다.");
        }
        return true;
    }
}
