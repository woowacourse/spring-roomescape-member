package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.AuthService;
import roomescape.auth.application.AuthorizationException;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;
import roomescape.member.application.InvalidRoleException;
import roomescape.member.domain.Role;

public class AdminRoleInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminRoleInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException();
        }
        Token token = extractTokenFrom(cookies);
        Payload payload = authService.getPayload(token);

        if (!payload.isAuthorizedFor(Role.ADMIN)) {
            throw new InvalidRoleException();
        }
        return true;
    }

    private Token extractTokenFrom(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return new Token(cookie.getValue());
            }
        }
        throw new AuthorizationException();
    }
}
