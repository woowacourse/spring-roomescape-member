package roomescape.controller.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.LoginService;

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
        Member foundMember = findMember(request);
        if (Role.mapTo(foundMember.getRole()) == ADMIN) {
            return true;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    private Member findMember(HttpServletRequest request) {
        return loginService.check(request.getCookies());
    }
}
