package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.jwt.JwtProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";

    public final JwtProvider jwtProvider;

    public CheckAdminInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }

        for (Cookie cookie : cookies) {
            if (TOKEN.equals(cookie.getName())) {
                AuthUser authUser = jwtProvider.parse(cookie.getValue());
                return checkAdmin(response, authUser);
            }
        }
        return false;
    }

    private boolean checkAdmin(HttpServletResponse response, AuthUser authUser) {
        if (authUser.isAdmin()) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
