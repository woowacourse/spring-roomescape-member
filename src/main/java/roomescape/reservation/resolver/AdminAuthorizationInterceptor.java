package roomescape.reservation.resolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.controller.response.MemberResponse;
import roomescape.member.resolver.UnauthenticatedException;
import roomescape.member.service.AutoService;

public class AdminAuthorizationInterceptor implements HandlerInterceptor {
    private final AutoService autoService;

    public AdminAuthorizationInterceptor(AutoService autoService) {
        this.autoService = autoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            MemberResponse member = autoService.findUserByToken(token);

            if (!"ADMIN".equals(member.role())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            return true;
        } catch (UnauthenticatedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
