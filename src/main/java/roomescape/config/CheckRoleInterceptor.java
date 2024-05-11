package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.model.Role;
import roomescape.service.AuthService;

public class CheckRoleInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckRoleInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Role role = authService.findRoleByCookie(request.getCookies());
        if (role == null || role.equals(Role.ADMIN)) {
            return true;
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return false;
    }
}
