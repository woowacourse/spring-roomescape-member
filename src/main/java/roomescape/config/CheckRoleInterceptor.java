package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.MemberService;

public class CheckRoleInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    CheckRoleInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);
        String subject = jwtTokenProvider.getSubject(token);
        long memberId = Long.parseLong(subject);

        Member member = memberService.getMemberById(memberId);
        boolean isAdmin = member.getRole().isAdmin();

        if (!(isAdmin)) {
            throw new ForbiddenAccessException("관리자만 접근 가능합니다.");
        }
        return true;
    }

    private static String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        return CookieParser.extractTokenFromCookie(cookies);
    }
}
