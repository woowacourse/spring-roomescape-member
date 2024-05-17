package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.infrastructure.TokenManager;
import roomescape.service.auth.AuthService;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final TokenManager tokenManager;

    public CheckLoginInterceptor(AuthService authService, TokenManager tokenManager) {
        this.authService = authService;
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String accessToken = tokenManager.getToken(request);

            if (authService.isAllowedMember(accessToken)) {
                return true;
            }

            response.sendRedirect("/login");
            return false;
        } catch (Exception e) {
            response.sendRedirect("/login");
            return false;
        }
    }
}
