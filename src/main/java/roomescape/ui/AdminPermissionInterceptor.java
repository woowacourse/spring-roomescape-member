package roomescape.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.exception.AuthorizationException;
import roomescape.member.model.Role;
import roomescape.member.service.MemberService;
import roomescape.util.JwtTokenHelper;

public class AdminPermissionInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenHelper jwtTokenHelper;

    public AdminPermissionInterceptor(MemberService memberService, JwtTokenHelper jwtTokenHelper) {
        this.memberService = memberService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!hasPermission(request, response)) {
            response.sendRedirect("/error/access-denied");
            return false;
        }
        return true;
    }

    private boolean hasPermission(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            final String token = jwtTokenHelper.extractTokenFromCookies(request.getCookies());
            final Long memberId = jwtTokenHelper.getPayloadClaimFromToken(token, "memberId", Long.class);
            final Role role = memberService.getMemberRoleById(memberId);

            return role == Role.ADMIN;
        } catch (IllegalArgumentException | AuthorizationException e) {
            response.sendRedirect("/login");
        }
        return false;
    }
}
