package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.RoleRequired;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.global.security.jwt.JwtTokenExtractor;
import roomescape.global.security.jwt.JwtTokenProvider;
import roomescape.member.entity.Role;

import java.util.Arrays;

@Component
public class RoleCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    public RoleCheckInterceptor(JwtTokenProvider jwtTokenProvider, JwtTokenExtractor jwtTokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenExtractor = jwtTokenExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
        if (roleRequired == null) {
            return true;
        }

        String token = jwtTokenExtractor.extractToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }

        Role role = jwtTokenProvider.getRole(token);
        if (isInvalidRole(roleRequired, role)) {
            throw new ForbiddenException("권한이 없는 사용자입니다.");
        }

        return true;
    }

    private boolean isInvalidRole(RoleRequired roleRequired, Role role) {
        return Arrays.stream(roleRequired.value())
                .noneMatch(requiredRole -> requiredRole == role);
    }
}
