package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AuthorizationException;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.MemberResponse;
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
        authService.isTokenValid(tokenResponse);
        String memberId = authService.extractMemberIdByToken(tokenResponse);
        MemberResponse memberResponse = loginMemberService.findById(Long.parseLong(memberId));
        if (!memberResponse.role().equals("ADMIN")) {
            throw new AuthorizationException("관리자가 아닙니다.");
        }
        return true;
    }
}
