package roomescape.config.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AdminAuthenticationException;
import roomescape.exception.MemberAuthenticationException;
import roomescape.member.domain.Member;
import roomescape.member.service.AuthService;

public class AdminAuthenticationInterceptor implements HandlerInterceptor {
    private static final String ACCESS_TOKEN_KEY = "token";

    private final AuthService authService;

    public AdminAuthenticationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String accessToken = findAccessToken(request);
            Member member = authService.findMember(accessToken);
            if (member.isAdmin()) {
                return true;
            }
        } catch (Exception ignored) {
        }

        response.setStatus(401);
        return false;
    }

    private String findAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS_TOKEN_KEY)) {
                return cookie.getValue();
            }
        }
        throw new MemberAuthenticationException();
    }
}
