package roomescape.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.infrastructure.JwtTokenProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        String role = jwtTokenProvider.getRoleByToken(token);
        if (role == null || !role.equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
