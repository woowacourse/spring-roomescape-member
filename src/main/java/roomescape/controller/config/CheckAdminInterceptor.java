package roomescape.controller.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AuthorizationException;
import roomescape.model.Member;
import roomescape.service.AuthService;

import java.util.Arrays;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO: refactoring code
        if (request.getServletPath().startsWith("/admin")) {
            Cookie token = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("token"))
                    .findFirst()
                    .orElseThrow(AuthorizationException::new);
            Member loginMember = authService.checkToken(token.getValue());
            if (loginMember.isNotAdmin()) {
                throw new AuthorizationException();
            }
        }
        return true;
    }
}
