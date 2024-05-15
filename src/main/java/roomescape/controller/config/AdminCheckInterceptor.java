package roomescape.controller.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.infrastructure.CookieManager;
import roomescape.infrastructure.auth.Token;
import roomescape.service.LoginService;

import java.util.NoSuchElementException;

import static roomescape.domain.member.Role.ADMIN;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final LoginService loginService;
    private final CookieManager cookieManager;

    public AdminCheckInterceptor(LoginService loginService, CookieManager cookieManager) {
        this.loginService = loginService;
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        try {
            Token token = cookieManager.getToken(request.getCookies());
            Member foundMember = loginService.check(token);
            if (foundMember.isRole(ADMIN)) {
                return true;
            }
        } catch (IllegalArgumentException | NoSuchElementException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return false;
    }
}
