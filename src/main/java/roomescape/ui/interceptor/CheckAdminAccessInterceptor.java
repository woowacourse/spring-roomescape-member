package roomescape.ui.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.Principal;
import roomescape.auth.exception.AccessDeniedException;

@Component
public class CheckAdminAccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Principal principal = (Principal) request.getAttribute(AuthenticationExtractInterceptor.PRINCIPAL_KEY_NAME);
        if (!principal.isAdmin()) {
            throw new AccessDeniedException();
        }
        return true;
    }
}
