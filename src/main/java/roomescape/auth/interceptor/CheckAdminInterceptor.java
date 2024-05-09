package roomescape.auth.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.AuthInfo;
import roomescape.auth.service.AuthService;
import roomescape.exception.AuthorizationException;
import roomescape.exception.BusinessException;
import roomescape.member.domain.Role;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private static final String SESSION_KEY = "token";

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = extractTokenFromCookie(request.getCookies());
        AuthInfo authInfo = null;
        try {
            authInfo = authService.fetchByToken(accessToken);
        } catch (BusinessException e) {
            throw new AuthorizationException();
        }
        if (authInfo == null || !authInfo.getRole().equals(Role.ADMIN)) {
            throw new AuthorizationException();
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_KEY)) {
                return cookie.getValue();
            }
        }
        throw new AuthorizationException();
    }
}
