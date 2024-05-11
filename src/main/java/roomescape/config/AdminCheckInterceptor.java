package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.LoginService;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public AdminCheckInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        Member member = loginService.findLoginMember(cookies);

        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
