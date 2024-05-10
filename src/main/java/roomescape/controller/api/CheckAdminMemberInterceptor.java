package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.CustomException2;
import roomescape.service.AuthService;

public class CheckAdminMemberInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminMemberInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {
        final var cookies = request.getCookies();
        final var token = extractTokenFromCookie(cookies);

        if (!authService.isAdmin(token)) {
            throw new CustomException2("어드민 유저가 아닙니다.");
        }
        return true;
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (final var cookie : cookies) {
            if (cookie.getName().equals("accessToken")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
