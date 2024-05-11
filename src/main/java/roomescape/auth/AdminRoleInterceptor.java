package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.role.RoleRepository;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;
    private final RoleRepository roleRepository;

    public AdminRoleInterceptor(TokenManager tokenManager, RoleRepository roleRepository) {
        this.tokenManager = tokenManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long memberId = tokenManager.getMemberIdFromCookies(request.getCookies());
        if (roleRepository.isAdminByMemberId(memberId)) {
            return true;
        }
        throw new AuthenticationException();
    }
}
