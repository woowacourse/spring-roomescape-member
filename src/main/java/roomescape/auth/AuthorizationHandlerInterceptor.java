package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.MemberNotFoundException;
import roomescape.infrastructure.JwtTokenProvider;

public class AuthorizationHandlerInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationHandlerInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        if (!requestURI.startsWith("/admin")) {
            return true;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/");
            return false;
        }
        String token = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(MemberNotFoundException::new)
                .getValue();
        if (Role.ADMIN != jwtTokenProvider.extractRole(token)) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
