package roomescape.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.controller.exception.AccessDeniedException;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.Role;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        try {
            String token = extractTokenFromCookie(request);
            Long memberId = jwtTokenProvider.getMemberId(token);
            Member member = memberRepository.getById(memberId);

            if (member.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("어드민 권한이 필요합니다.");
            }

            return true;
        } catch (Exception e) {
            throw new AccessDeniedException(e);
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> TOKEN.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
