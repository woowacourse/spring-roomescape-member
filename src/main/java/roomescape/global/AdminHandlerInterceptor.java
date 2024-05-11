package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AdminHandlerInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String accessToken = jwtTokenProvider.parseToken(request);
        Member findMember = memberService.findById(Long.valueOf(jwtTokenProvider.getPayload(accessToken)));
        if (findMember.isAdminMember()) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
