package roomescape.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.LoginService;
import roomescape.application.service.MemberService;
import roomescape.domain.Member;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final MemberService memberService;

    public CheckLoginInterceptor(LoginService loginService, MemberService memberService) {
        this.loginService = loginService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getCookies() == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        Long memberId = loginService.extractMemberIdFromRequest(request);
        Member member = memberService.findById(memberId);

        if (member.isAdmin()) {
            return true;
        }
        
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
