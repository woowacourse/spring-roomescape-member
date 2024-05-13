package roomescape.presentation.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class PermissionCheckInterceptor implements HandlerInterceptor {
    private final RequestPayloadContext context;

    public PermissionCheckInterceptor(ObjectProvider<RequestPayloadContext> contextProvider) {
        this.context = contextProvider.getObject();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(PermissionRequired.class)) {
            return true;
        }
        PermissionRequired permissionRequired = method.getAnnotation(PermissionRequired.class);
        context.setMemberRoleIfNotPresent(request);
        context.validatePermission(permissionRequired.value());
        return true;
    }
}
