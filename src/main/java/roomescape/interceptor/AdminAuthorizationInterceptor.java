package roomescape.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.infrastructure.JwtProvider;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor authorizationExtractor;
    private final JwtProvider jwtProvider;

    public AdminAuthorizationInterceptor(AuthorizationExtractor authorizationExtractor, JwtProvider jwtProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = authorizationExtractor.extractToken(request);

        if (token == null || !jwtProvider.isValidateToken(token)) {
            return AuthorizationResponseHandler.redirectLogin(response);
        }

        if (!isAdmin(token)) {
            return AuthorizationResponseHandler.responseUnauthorized(response);
        }

        return true;
    }

    private boolean isAdmin(String token) {
        Claims claims = jwtProvider.getClaims(token);
        String role = claims.get("role", String.class);
        return Role.valueOf(role) == Role.ADMIN;
    }
}
