package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.AuthorizationException;
import roomescape.member.domain.Role;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        validateCookie(cookies);

        String token = jwtTokenProvider.extractTokenFromCookie(cookies);

        String role = jwtTokenProvider.getRole(token);
        validateRole(role);

        return true;
    }

    private void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 없습니다.");
        }
    }

    private void validateRole(String rawRole) {
        Role role = makeRole(rawRole);
        if (role.isMember()) {
            throw new AuthorizationException("권한이 없습니다.");
        }
    }

    private Role makeRole(String rawRole) {
        try {
            return Role.valueOf(rawRole);
        } catch (IllegalArgumentException ex) {
            throw new AuthorizationException("권한이 없습니다.");
        }
    }
}
