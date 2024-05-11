package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.MemberRole;
import roomescape.exception.auth.AccessDeniedException;
import roomescape.service.helper.CookieExtractor;
import roomescape.service.helper.JwtTokenProvider;

public class CheckAdminInterceptor implements HandlerInterceptor {
    private final CookieExtractor cookieExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminInterceptor(CookieExtractor cookieExtractor, JwtTokenProvider jwtTokenProvider) {
        this.cookieExtractor = cookieExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = cookieExtractor.getToken(request.getCookies());
        MemberRole role = jwtTokenProvider.getMemberRole(token);
        if (role != MemberRole.ADMIN) {
            throw new AccessDeniedException();
        }
        return true;
    }
}
