package roomescape.ui.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthenticationInfoExtractor;
import roomescape.auth.exception.AccessDeniedException;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

public class CheckAdminAccessInterceptor implements HandlerInterceptor {
    private final AuthenticationInfoExtractor authenticationInfoExtractor;
    private final MemberRepository memberRepository;

    public CheckAdminAccessInterceptor(AuthenticationInfoExtractor authenticationInfoExtractor,
                                       MemberRepository memberRepository) {
        this.authenticationInfoExtractor = authenticationInfoExtractor;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long memberId = authenticationInfoExtractor.extractMemberId(request);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(AccessDeniedException::new);
        if (!member.isAdmin()) {
            throw new AccessDeniedException();
        }
        return true;
    }
}
