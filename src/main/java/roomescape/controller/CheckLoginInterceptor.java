package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthenticationService;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    public CheckLoginInterceptor(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String accessToken = extractTokenFromCookie(cookies);
        if (accessToken == null) {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
        Long id = authenticationService.validateTokenAndGetId(accessToken);
        System.out.println("id = " + id);
        request.setAttribute("memberId", id);
        return true;
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