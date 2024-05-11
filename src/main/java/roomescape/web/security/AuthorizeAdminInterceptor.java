package roomescape.web.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedMemberProfile;
import roomescape.service.auth.UnauthorizedException;

public class AuthorizeAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final CookieTokenExtractor extractor;

    public AuthorizeAdminInterceptor(AuthService authService, CookieTokenExtractor extractor) {
        this.authService = authService;
        this.extractor = extractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String cookieToken = extractor.extract(request);
        if (cookieToken == null) {
            throw new UnauthorizedException();
        }

        AuthenticatedMemberProfile profile = authService.authorize(cookieToken);
        if (!profile.isAdmin()) {
            throw new UnauthorizedException();
        }

        return true;
    }
}
