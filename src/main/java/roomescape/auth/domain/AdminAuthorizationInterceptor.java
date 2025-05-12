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

        if (!request.getRequestURI().startsWith("/admin")) {
            return true;
        }

        String token = extractor.extract(request);
        Member member = authService.findMemberByToken(token);

        if (member.getRole().equals(Role.ADMIN)) {
            return true;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
