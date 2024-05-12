package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;
import roomescape.exception.AuthorizationException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        validateCookie(cookies);
        
        Member member = authService.findMemberByCookie(cookies);
        validateRoleIsAdmin(member.getRole());

        return true;
    }

    private void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 없습니다.");
        }
    }

    private void validateRoleIsAdmin(Role role) {
        if (role.isNotAdmin()) {
            throw new AuthorizationException("권한이 없습니다.");
        }
    }
}
