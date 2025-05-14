package roomescape.auth.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public AdminAuthorizationInterceptor(final AuthService authService, final CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        String token = extractor.extract(request);

        if (token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        if (isAdmin(token)) {
            return true;
        }

        response.setStatus(HttpStatus.FORBIDDEN.value());
        return false;
    }

    private boolean isAdmin(String token) {
        Member member = authService.findMemberByToken(token);
        return member.getRole().equals(Role.ADMIN);
    }
}
