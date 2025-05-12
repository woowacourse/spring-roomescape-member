package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.repository.MemberRepository;

public class AuthInterceptor implements HandlerInterceptor {
    private final MemberRepository memberRepository;

    public AuthInterceptor(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Member member = (Member) session.getAttribute("LOGIN_MEMBER");
        if (member == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 멤버입니다."));
        String uri = request.getRequestURI();
        if (uri.startsWith("/admin") && member.getRole() != MemberRole.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 필요합니다.");
            return false;
        }
        request.setAttribute("LOGIN_MEMBER", foundMember);
        return true;
    }
}
