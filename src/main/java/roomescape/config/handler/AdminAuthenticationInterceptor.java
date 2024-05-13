package roomescape.config.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.controller.TokenCookieConvertor;
import roomescape.auth.dto.LoggedInMember;
import roomescape.auth.dto.RequestCookies;
import roomescape.auth.exception.AdminAuthenticationException;
import roomescape.auth.service.AuthService;

@Component
public class AdminAuthenticationInterceptor implements HandlerInterceptor {
    private final TokenCookieConvertor tokenCookieConvertor;
    private final AuthService authService;

    public AdminAuthenticationInterceptor(TokenCookieConvertor tokenCookieConvertor, AuthService authService) {
        this.tokenCookieConvertor = tokenCookieConvertor;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenCookieConvertor.getToken(request.getCookies());
        LoggedInMember member = authService.findLoggedInMember(token);
        if (member.isAdmin()) {
            return true;
        }
        throw new AdminAuthenticationException();
    }
}
