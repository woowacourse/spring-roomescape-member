package roomescape.controller.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.controller.api.member.dto.LoginMemberInfo;
import roomescape.controller.api.member.dto.MemberResponse;
import roomescape.model.Role;
import roomescape.service.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckAdminInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final LoginMemberInfo loginMemberInfo = findLoginMember(request);
        if (loginMemberInfo == null || !loginMemberInfo.isAdmin()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private LoginMemberInfo findLoginMember(final HttpServletRequest request) {
        final String token = ControllerSupports.findCookieByKey(request, "token");
        if (!memberService.isValidToken(token)) {
            return null;
        }
        final MemberResponse response = memberService.findByToken(token);
        return new LoginMemberInfo(response.id(), response.name(), response.email(), Role.findByName(response.role()));
    }
}
