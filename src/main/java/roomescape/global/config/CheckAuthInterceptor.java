package roomescape.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.AuthService;
import roomescape.application.service.MemberService;
import roomescape.domain.AuthMember;
import roomescape.domain.Member;

public class CheckAuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthService authService;

    public CheckAuthInterceptor(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getCookies() == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        AuthMember authMember = authService.extractAuthMemberFromRequest(request);
        if (authMember.isNotAdmin()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        if (isNotAdminMember(authMember)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private boolean isNotAdminMember(AuthMember authMember) {
        Member member = memberService.getMemberById(authMember.getId());
        return member.isNotAdmin();
    }
}
