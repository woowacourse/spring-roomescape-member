package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.JwtTokenProvider;
import roomescape.Role;
import roomescape.service.AuthorizationException;

@Component
public class CheckAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    public CheckAuthInterceptor(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenProvider.extractTokenFromCookie(request.getCookies());
        Role role = Role.valueOf(tokenProvider.getRoleFromToken(token));
        if (role != Role.ADMIN) {
            throw new AuthorizationException("접근 권한이 없습니다.");
        }

        return true;
    }
}
