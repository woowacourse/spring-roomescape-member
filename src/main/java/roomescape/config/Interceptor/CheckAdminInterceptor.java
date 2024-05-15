package roomescape.config.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;
import roomescape.service.exception.AuthorizationException;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = authService.extractTokenFromCookie(request.getCookies());
        if (authService.isAdminByToken(token)) {
            return true;
        }
        throw new AuthorizationException("접근할 수 없습니다.");
    }
}
