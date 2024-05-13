package roomescape.presentation.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.role.Role;

public class AdminRoleInterceptor implements HandlerInterceptor {
    private final RequestPayloadContext context;

    public AdminRoleInterceptor(ObjectProvider<RequestPayloadContext> contextProvider) {
        this.context = contextProvider.getObject();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        context.setMemberRoleIfNotPresent(request);
        context.validatePermission(Role.ADMIN);
        return true;
    }
}
