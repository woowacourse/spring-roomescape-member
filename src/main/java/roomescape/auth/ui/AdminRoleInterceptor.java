package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.AuthService;
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
        Token token = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                token = new Token(cookie.getValue());
            }
        }

        Payload payload = authService.getPayload(token);
        if (payload.role() != Role.ADMIN) {
            throw new InvalidRoleException();
        }
        return true;
    }
}
