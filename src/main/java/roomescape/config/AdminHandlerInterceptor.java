package roomescape.config;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.domain.Role;
import roomescape.exception.member.AuthenticationFailureException;
import roomescape.exception.member.AuthorizationFailureException;
import roomescape.service.security.JwtUtils;

public class AdminHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie tokenCookie = extractCookie(request, "token");
        String token = tokenCookie.getValue();
        Role role = JwtUtils.decodeRole(token);
        if (!role.isAdmin()) {
            throw new AuthorizationFailureException();
        }
        return true;
    }

    private Cookie extractCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElseThrow(AuthenticationFailureException::new);
    }

}
