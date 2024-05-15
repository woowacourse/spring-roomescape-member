package roomescape.config;

import jakarta.servlet.http.Cookie;
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
        Optional<String> token = extractToken(request);
        if (token.isEmpty()) {
            throw new TokenValidationFailureException("토큰이 존재하지 않습니다.");
        }

        String subject = jwtTokenProvider.getSubject(token.get());
        long memberId = Long.parseLong(subject);
        Member member = memberService.getMemberById(memberId);
        boolean isAdmin = member.getRole().isAdmin();

        if (!(isAdmin)) {
            throw new ForbiddenAccessException("관리자만 접근 가능합니다.");
        }
        return true;
    }

    private static Optional<String> extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        return CookieUtil.extractTokenFromCookie(cookies);
    }
}
