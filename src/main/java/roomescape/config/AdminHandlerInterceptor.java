package roomescape.config;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.domain.Role;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.exception.member.AuthorizationFailureException;
import roomescape.service.security.JwtProvider;

public class AdminHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie tokenCookie = extractCookie(request, "token");
        String token = tokenCookie.getValue();
        Role role = JwtProvider.decodeRole(token);
        if (!role.isAdmin()) {
            throw new AuthorizationFailureException();
        }
        return true;
    }

    private Cookie extractCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return new Cookie("token", "");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElseThrow(AuthenticationFailureException::new);
    }

}
