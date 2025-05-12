package roomescape.global.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.JwtTokenProvider;
import roomescape.global.exception.custom.ForbiddenException;
import roomescape.global.exception.custom.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminCheckInterceptor(final MemberRepository memberRepository,
                                 final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response, final Object handler
    ) throws Exception {
        final Cookie[] cookies = request.getCookies();
        final String token = getToken(cookies);
        final long id = jwtTokenProvider.getId(token);
        final Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("확인할 수 없는 사용자입니다."));
        if (!member.hasRole(Role.ADMIN)) {
            throw new ForbiddenException("접근 권한이 없습니다.");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private static String getToken(final Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("쿠키를 입력해 주세요.");
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("토큰을 입력해주세요."))
                .getValue();
    }
}
