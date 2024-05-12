package roomescape.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.RequestCookies;
import roomescape.auth.exception.AdminAuthenticationException;
import roomescape.auth.service.AuthService;

@Component
public class AdminAuthenticationInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AdminAuthenticationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestCookies cookies = new RequestCookies(request.getCookies());
        LoggedInMember member = authService.findLoggedInMember(cookies);
        if (member.isAdmin()) {
            return true;
        }
        throw new AdminAuthenticationException();
    }
}
