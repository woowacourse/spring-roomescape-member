package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckLoginInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {
        Cookie[] cookies = request.getCookies();
        boolean isAdmin = authService.isAdmin(cookies);
        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다.");
        }
        return authService.isAdmin(cookies);
    }
}
