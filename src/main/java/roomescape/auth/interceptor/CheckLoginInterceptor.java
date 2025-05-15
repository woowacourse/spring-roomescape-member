package roomescape.auth.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;
import roomescape.auth.util.CookieHandler;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckLoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        final Cookie[] cookies = request.getCookies();
        final String token = CookieHandler.extractTokenFromCookies(cookies);

        if (!authService.isAdmin(token)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }
}
