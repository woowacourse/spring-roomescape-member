package roomescape.global.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.service.AuthService;
import roomescape.application.service.MemberService;
import roomescape.domain.Member;
import roomescape.domain.Role;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthService authService;

    public CheckLoginInterceptor(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            validateAdminAccess(request);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    private void validateAdminAccess(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new IllegalArgumentException();
        }

        Role role = authService.extractRoleFromRequest(request);
        validateAdminRole(role);

        Long memberId = authService.extractMemberIdFromRequest(request);
        Member member = memberService.findById(memberId);
        validateAdminRole(member);
    }

    private void validateAdminRole(Role role) {
        if (role.isNotAdmin()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateAdminRole(Member member) {
        if (member.isNotAdmin()) {
            throw new IllegalArgumentException();
        }
    }
}
