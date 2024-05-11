package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.AuthService;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public CheckAdminInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Member member = null;
        final Cookie[] cookies = request.getCookies();
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                final String token = cookie.getValue();
                member = authService.findMemberByToken(token);
            }
        }

        if (member == null || !member.getRole().name().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
