package roomescape.configuration.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.domain.Member;
import roomescape.exception.AuthenticationException;
import roomescape.service.AuthService;
import roomescape.util.CookieUtil;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthHandlerInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = CookieUtil.requireNonnull(request.getCookies(), AuthenticationException::new);
        Member member = authService.findFromCookies(cookies);
        if (!member.getRole().isAdmin()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
