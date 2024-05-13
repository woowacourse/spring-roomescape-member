package roomescape.global.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.RoomEscapeException;
import roomescape.global.jwt.JwtProvider;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static roomescape.global.exception.ExceptionMessage.CANNOT_ACCESS;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager = new CookieManager();
    private final JwtProvider jwtProvider;

    public AdminCheckInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        List<Cookie> cookies = cookieManager.extractCookies(request);

        String token = cookieManager.extractToken(cookies);
        AuthUser authUser = jwtProvider.parse(token);
        return checkAdmin(authUser);

    }

    private boolean checkAdmin(AuthUser authUser) {
        if (authUser.isAdmin()) {
            return true;
        }
        throw new RoomEscapeException(FORBIDDEN, CANNOT_ACCESS.getMessage());
    }
}
