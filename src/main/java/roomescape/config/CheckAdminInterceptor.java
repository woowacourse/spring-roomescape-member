package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.MemberService;
import roomescape.util.CookieUtil;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckAdminInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new IllegalAccessException("[ERROR] 유효한 사용자가 아닙니다.");
        }

        Long memberId = CookieUtil.getSubjectFromCookie(cookies);
        Member member = memberService.findMemberById(memberId);

        if (isInvalidMember(member)) {
            throw new IllegalAccessException("[ERROR] 유효한 사용자가 아닙니다.");
        }

        return true;
    }

    private boolean isInvalidMember(Member member) {
        return member == null || member.isNotAdmin();
    }
}
