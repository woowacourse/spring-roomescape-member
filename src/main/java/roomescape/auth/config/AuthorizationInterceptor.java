package roomescape.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.vo.Authorization;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthorizationInterceptor(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        String token = extractTokenFromCookies(request);

        if (token != null && jwtUtil.validateToken(token)) {
            final Authorization authorization = jwtUtil.getAuthorization(token);
            request.setAttribute("authorization", authorization);
            return true;
        }

        return false;
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
