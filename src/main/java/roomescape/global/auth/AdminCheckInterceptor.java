package roomescape.global.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final AuthCookie authCookie;

    public AdminCheckInterceptor(final AuthService authService, final AuthCookie authCookie) {
        this.authService = authService;
        this.authCookie = authCookie;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response, final Object handler
    ) throws Exception {
        final String token = authCookie.getValue(request.getCookies());
        authService.checkAdminMember(token);
        return true;
    }
}
