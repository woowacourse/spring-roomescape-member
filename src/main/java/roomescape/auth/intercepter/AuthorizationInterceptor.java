package roomescape.auth.intercepter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.annotation.RequiredRoles;
import roomescape.auth.jwt.domain.Jwt;
import roomescape.auth.jwt.manager.JwtManager;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserRole;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";

    private final JwtManager jwtManager;

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {

        // 정적 리소스 통과
        if (!(handler instanceof final HandlerMethod method)) {
            return true;
        }

        final List<UserRole> requiredRoles = getRequiredRoles(method);

        if (requiredRoles == null || requiredRoles.isEmpty()) {
            return true;
        }

        final String rawToken = request.getHeader(AUTHORIZATION);
        final Jwt accessToken = Jwt.from(rawToken);

        final Claims claims = jwtManager.parse(accessToken);

        final UserRole role = claims.get("role", UserRole.class);

        if (requiredRoles.contains(role)) {
            return true;
        }

        throw new ForbiddenException(
                claims.get("id", UserId.class),
                role,
                requiredRoles
        );
    }

    private List<UserRole> getRequiredRoles(final HandlerMethod method) {
        RequiredRoles roles = getMethodAnnotation(method);
        if (roles == null) {
            roles = getClassAnnotation(method);
        }
        if (roles == null) {
            return null;
        }
        return Stream.of(roles.value())
                .toList();
    }

    private RequiredRoles getClassAnnotation(final HandlerMethod method) {
        return method.getBeanType().getAnnotation(RequiredRoles.class);
    }

    private RequiredRoles getMethodAnnotation(final HandlerMethod method) {
        return method.getMethodAnnotation(RequiredRoles.class);
    }
}
