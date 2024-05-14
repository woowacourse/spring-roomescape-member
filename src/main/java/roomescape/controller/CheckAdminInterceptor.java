package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.constant.Role;
import roomescape.exception.AuthorizationException;
import roomescape.util.JwtTokenProvider;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenProvider.extractTokenFromCookie(request.getCookies());
        String role = tokenProvider.getRoleFromToken(token);
        if (Role.valueOf(role) != Role.ADMIN) {
            throw new AuthorizationException("접근 권한이 없습니다.");
        }

        return true;
    }
}
