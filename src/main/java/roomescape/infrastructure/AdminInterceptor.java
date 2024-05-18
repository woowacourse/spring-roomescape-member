package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.MemberService;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public AdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Member member = memberService.createMember(request.getCookies());

        if (member == null || member.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("관리자만 접근할 수 있습니다.");
        }

        return true;
    }
}
