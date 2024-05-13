package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.LoginMemberResponse;
import roomescape.service.dto.TokenResponse;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    public RoleInterceptor(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        TokenResponse tokenResponse = authService.extractTokenByCookies(request);
        String email = authService.extractEmailByToken(tokenResponse);
        LoginMemberResponse loginMemberResponse = loginMemberService.findByEmail(email);
        authService.isTokenValid(tokenResponse);
        if (loginMemberResponse == null || !loginMemberResponse.role().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
