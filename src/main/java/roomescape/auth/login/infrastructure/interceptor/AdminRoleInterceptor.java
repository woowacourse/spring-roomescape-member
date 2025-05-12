package roomescape.auth.login.infrastructure.interceptor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.RequiredTypeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.login.infrastructure.token.JwtTokenManager;
import roomescape.auth.login.infrastructure.token.TokenExtractor;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {

    public static final String ADMIN_STRING = "ADMIN";

    private final JwtTokenManager jwtTokenManager;

    public AdminRoleInterceptor(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = TokenExtractor.extract(request);

        try {
            String role = jwtTokenManager.getRole(token);
            validateRoleIsAdmin(role);

            return true;
        } catch (MalformedJwtException malformedJwtException) {
            throw new UnauthorizedException("유효한 토큰이 아닙니다.");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new UnauthorizedException("토큰이 만료되었습니다.");
        } catch (IllegalArgumentException argumentException) {
            throw new UnauthorizedException("토큰이 비었습니다.");
        } catch (RequiredTypeException requiredTypeException) {
            throw new UnauthorizedException("해당 claim을 찾을 수 없습니다.");
        } catch (JwtException jwtException) {
            throw new UnauthorizedException(jwtException.getMessage());
        }
    }

    private static void validateRoleIsAdmin(final String role) {
        if (!role.equals(ADMIN_STRING)) {
            throw new ForbiddenException("관리자가 아닙니다.");
        }
    }
}
