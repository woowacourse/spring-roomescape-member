package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.AuthorizationException;

import java.util.Arrays;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        validateCookie(cookies);

        String token = extractTokenFromCookie(cookies);

        String role = jwtTokenProvider.getRole(token);
        validateRole(role);

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("토큰이 없습니다."));
    }

    private void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 없습니다.");
        }
    }

    private void validateRole(String role) {
        if (role.isBlank() || role.equals("USER")) {
            throw new AuthorizationException("권한이 없습니다.");
        }
    }
}
