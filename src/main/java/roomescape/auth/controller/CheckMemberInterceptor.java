package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.AuthorizationException;

import java.util.Arrays;

public class CheckMemberInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public CheckMemberInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromCookie(request.getCookies());
        String role = jwtTokenProvider.getRole(token);

        if (role.isBlank() || role.equals("USER")) {
            throw new AuthorizationException("권한이 없습니다.");
        }

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }
}
