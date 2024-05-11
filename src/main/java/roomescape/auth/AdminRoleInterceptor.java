package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.AuthenticationException;
import roomescape.domain.role.RoleRepository;

public class AdminRoleInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;
    private final RoleRepository roleRepository;

    public AdminRoleInterceptor(TokenManager tokenManager, RoleRepository roleRepository) {
        this.tokenManager = tokenManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = AuthInformationExtractor.extractToken(request);
        long memberId = tokenManager.getMemberIdFrom(token);
        if (roleRepository.isAdminByMemberId(memberId)) {
            return true;
        }
        throw new AuthenticationException();
    }
}
