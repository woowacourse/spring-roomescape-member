package roomescape.member.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.member.AuthService;
import roomescape.member.JwtTokenProvider;

public class CheckExpiredTokenInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public CheckExpiredTokenInterceptor(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = authService.extractToken(request);
        if (jwtTokenProvider.isExpiredToken(accessToken)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
