package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.MemberService;

import java.util.Optional;

public class CheckRoleInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    CheckRoleInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request)
                .orElseThrow(() -> new TokenValidationFailureException("토큰이 존재하지 않습니다."));
        long memberId = Long.parseLong(jwtTokenProvider.getSubject(token));
        Member member = memberService.getMemberById(memberId);

        if (member.isNotAdmin()) {
            throw new ForbiddenAccessException();
        }
        return true;
    }

    private static Optional<String> extractToken(HttpServletRequest request) {
        return TokenExtractor.extractTokenFromCookie(request.getCookies());
    }
}
