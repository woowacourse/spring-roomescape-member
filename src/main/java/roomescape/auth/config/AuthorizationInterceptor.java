package roomescape.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthRequired;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.vo.Authorization;
import roomescape.exception.impl.NotAuthenticatedException;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthorizationInterceptor(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (!requireAuthorization(handler)) {
            return true;
        }

        String token = extractTokenFromCookies(request);

        if (token != null && jwtUtil.validateToken(token)) {
            final Authorization authorization = jwtUtil.getAuthorization(token);
            request.setAttribute("authorization", authorization);
            return true;
        }

        throw new NotAuthenticatedException();
    }

    private static boolean requireAuthorization(final Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }

        return handlerMethod.hasMethodAnnotation(AuthRequired.class);
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
