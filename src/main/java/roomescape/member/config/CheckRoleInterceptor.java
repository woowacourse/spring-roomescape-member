package roomescape.member.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.member.AuthService;
import roomescape.member.service.MemberService;

public class CheckRoleInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final MemberService memberService;

    public CheckRoleInterceptor(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = authService.extractToken(request);
        long memberId = authService.findMemberIdByToken(accessToken);

        if (memberService.isNotAdmin(memberId)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
