package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.RoomEscapeException;
import roomescape.global.jwt.JwtProvider;

import java.util.List;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager = new CookieManager();
    private final JwtProvider jwtProvider;

    public CheckAdminInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        List<Cookie> cookies = cookieManager.extractCookies(request);

        try {
            String token = cookieManager.extractToken(cookies);
            AuthUser authUser = jwtProvider.parse(token);
            return checkAdmin(response, authUser);
        } catch (RoomEscapeException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    private boolean checkAdmin(HttpServletResponse response, AuthUser authUser) {
        if (authUser.isAdmin()) {
            return true;
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return false;
    }
}
