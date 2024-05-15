package roomescape.controller.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.service.LoginService;

import java.util.NoSuchElementException;

import static roomescape.domain.member.Role.ADMIN;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final LoginService loginService;

    public AdminCheckInterceptor(final LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        try {
            Member foundMember = loginService.check(request.getCookies());
            if (foundMember.isRole(ADMIN)) {
                return true;
            }
        } catch (IllegalArgumentException | NoSuchElementException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return false;
    }
}
