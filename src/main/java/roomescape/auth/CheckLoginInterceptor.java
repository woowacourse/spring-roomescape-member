package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckLoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        final Cookie[] cookies = request.getCookies();

        final String token = authService.extractTokenFromCookie(cookies);
        Member member = authService.findMember(token);

        if (member == null || !member.getRole().equals("Admin")) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
