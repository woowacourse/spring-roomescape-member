package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.LoginService;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public AdminCheckInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (loginService.isAdminMember(cookies)) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
