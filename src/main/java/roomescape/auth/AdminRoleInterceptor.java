package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.AuthenticationException;
import roomescape.domain.role.MemberRole;

public class AdminRoleInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;

    public AdminRoleInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestToken = AuthInformationExtractor.extractToken(request);
        MemberRole memberRole = tokenManager.extract(requestToken);
        if (memberRole.isAdmin()) {
            return true;
        }
        throw new AuthenticationException();
    }
}
