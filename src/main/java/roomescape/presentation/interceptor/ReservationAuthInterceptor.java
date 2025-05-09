package roomescape.presentation.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;

@Component
public class ReservationAuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public ReservationAuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();
        if (cookies == null || !containsCookieForToken(cookies)) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private boolean containsCookieForToken(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .anyMatch(cookie -> cookie.getName().equals("token"));
    }
}
