package roomescape.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.AuthService;
import roomescape.application.service.MemberService;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthService authService;

    public CheckLoginInterceptor(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getCookies() == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        LoginMember loginMember = authService.extractLoginMemberFromRequest(request);
        if (loginMember.isNotAdmin()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        if (isNotAdminMember(loginMember)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private boolean isNotAdminMember(LoginMember loginMember) {
        Member member = memberService.getMemberById(loginMember.getId());
        return member.isNotAdmin();
    }
}
