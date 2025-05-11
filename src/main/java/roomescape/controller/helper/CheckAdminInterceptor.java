package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.model.Member;
import roomescape.service.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckAdminInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final Member member = findMember(request);
        if (member == null || !member.isAdmin()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private Member findMember(final HttpServletRequest request) {
        final String token = ControllerSupports.findCookieByKey(request, "token");
        if (!memberService.isValidToken(token)) {
            return null;
        }
        return memberService.findByToken(token).toEntity();
    }
}
