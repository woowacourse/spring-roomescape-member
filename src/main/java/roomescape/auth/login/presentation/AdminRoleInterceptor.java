package roomescape.auth.login.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.login.infrastructure.JwtTokenManager;

public class AdminRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(401);
            return false;
        }

        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .get();
        if (tokenCookie == null) {
            response.setStatus(401);
            return false;
        }

        String role = JwtTokenManager.getRole(tokenCookie.getValue());
        if (!role.equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
