package roomescape.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.global.exception.IllegalRequestException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
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

        Long extractedMemberId = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .map(cookie -> Long.valueOf(jwtTokenProvider.getPayload(cookie.getValue())))
                .findAny()
                .orElseThrow(() -> new IllegalRequestException("요청에 토큰이 포함되어 있지 않습니다"));

        Member findMember = memberService.findById(extractedMemberId);
        if (findMember.getRole() != Role.ADMIN) {
            return false;
        }
        return true;
    }
}
