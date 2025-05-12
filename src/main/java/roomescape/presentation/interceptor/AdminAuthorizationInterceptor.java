package roomescape.presentation.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.AuthService;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminAuthorizationInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return reject(response);
        }

        String token = extractTokenFromCookie(cookies);
        boolean isAdmin = authService.checkIfAdmin(token);
        if (!isAdmin) {
            return reject(response);
        }
        return true;
    }

    private boolean reject(HttpServletResponse response) {
        response.setStatus(401);
        return false;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
