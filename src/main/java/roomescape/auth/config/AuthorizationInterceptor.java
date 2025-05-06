package roomescape.auth.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthRequired;
import roomescape.auth.jwt.JwtUtil;
import roomescape.business.model.vo.Authorization;
import roomescape.business.model.vo.UserRole;
import roomescape.exception.impl.ForbiddenException;
import roomescape.exception.impl.NotAuthenticatedException;

import java.util.Arrays;
import java.util.List;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthorizationInterceptor(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        if (!handlerMethod.hasMethodAnnotation(AuthRequired.class)) {
            return true;
        }
        AuthRequired authRequired = handlerMethod.getMethodAnnotation(AuthRequired.class);
        if (authRequired == null) {
            return true;
        }
        String token = extractTokenFromCookies(request);
        validateAuthenticated(token);
        Authorization authorization = jwtUtil.getAuthorization(token);
        validateUserRole(authorization, authRequired);
        request.setAttribute("authorization", authorization);
        return true;
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

    private void validateAuthenticated(final String token) {
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new NotAuthenticatedException();
        }
    }

    private static void validateUserRole(final Authorization authorization, final AuthRequired authRequired) {
        UserRole userRole = authorization.userRole();
        final List<UserRole> allowedRoles = Arrays.asList(authRequired.value());

        if (!allowedRoles.contains(userRole)) {
            throw new ForbiddenException();
        }
    }
}
