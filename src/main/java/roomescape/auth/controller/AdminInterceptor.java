package roomescape.auth.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.annotation.RequiredAdmin;
import roomescape.auth.service.JwtTokenHandler;
import roomescape.common.exception.AuthenticationException;
import roomescape.member.domain.Role;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtTokenHandler jwtTokenHandler;

    public AdminInterceptor(final JwtTokenHandler jwtTokenHandler) {
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequiredAdmin requiredAdmin = handlerMethod.getMethodAnnotation(RequiredAdmin.class);
        if (requiredAdmin == null) {
            return true;
        }
        String token = jwtTokenHandler.extractTokenValue(request);
        jwtTokenHandler.validateToken(token);
        Role role = jwtTokenHandler.getRole(token);

        if (role != Role.ADMIN) {
            throw new AuthenticationException("권한이 존재하지 않습니다.");
        }
        return true;
    }
}
