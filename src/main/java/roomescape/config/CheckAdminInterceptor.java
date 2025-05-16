package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthService authService;

    public CheckAdminInterceptor(final MemberService memberService, final AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
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

        Long memberId = authService.getSubjectFromCookie(cookies);
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
