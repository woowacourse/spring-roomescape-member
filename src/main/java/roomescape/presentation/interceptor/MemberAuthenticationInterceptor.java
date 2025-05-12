package roomescape.presentation.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.AuthService;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;

public class MemberAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public MemberAuthenticationInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String token = extractTokenFromCookie(request.getCookies());
        Member member = authService.loadMemberByAuthInformation(token);
        request.setAttribute("member", member);

        if (member == null) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}
