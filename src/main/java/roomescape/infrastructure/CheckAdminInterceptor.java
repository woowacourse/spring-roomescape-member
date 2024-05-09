package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.infrastructure.auth.Token;
import roomescape.service.serviceimpl.LoginService;
import roomescape.service.serviceimpl.MemberService;

import static roomescape.domain.member.Role.ADMIN;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;
    private final MemberService memberService;
    private final LoginService loginService;

    public CheckAdminInterceptor(
            final CookieManager cookieManager,
            final MemberService memberService,
            final LoginService loginService
    ) {
        this.cookieManager = cookieManager;
        this.memberService = memberService;
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        Member foundMember = findMember(request);
        if (Role.mapTo(foundMember.getRole()) == ADMIN) {
            return true;
        }
        throw new IllegalAccessException("[ERROR] 어드민 페이지는 관리자만 접속할 수 있어요");
    }

    private Member findMember(HttpServletRequest request) {
        Token token = cookieManager.getToken(request.getCookies());
        Long memberId = loginService.findMemberIdByToken(token);
        return memberService.findMemberById(memberId);
    }
}
