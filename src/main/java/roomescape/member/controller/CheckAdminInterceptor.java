package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.CustomUnauthorized;
import roomescape.member.service.AuthService;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getCookies() == null) {
            throw new CustomException(CustomUnauthorized.NOT_LOGIN);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                String email = authService.parseEmail(cookie);
                return authService.checkAdmin(email);
            }
        }
        throw new CustomException(CustomUnauthorized.NOT_AUTHORIZED);
    }
}
