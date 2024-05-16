package roomescape.controller.intercepter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.domain.Role;
import roomescape.domain.exception.AuthFailException;
import roomescape.service.TokenService;

public class CheckAdminLoginInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public CheckAdminLoginInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookie = request.getCookies();
        if (cookie == null) {
            throw new AuthFailException("접근할 수 없는 페이지입니다.");
        }

        Role role = tokenService.findRole(cookie);
        if (role.isAdmin()) {
            return true;
        }
        throw new AuthFailException("접근할 수 없는 페이지입니다.");
    }
}
