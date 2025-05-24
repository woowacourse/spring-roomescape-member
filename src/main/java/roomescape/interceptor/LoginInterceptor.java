package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.LoginMember;
import roomescape.domain.Role;
import roomescape.exception.InvalidCredentialsException;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.service.MemberService;
import roomescape.util.CookieExtractor;
import roomescape.util.CookieKeys;
import roomescape.util.JwtTokenProvider;

public class LoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieExtractor authorizationExtractor;

    public LoginInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authorizationExtractor = new CookieExtractor();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = authorizationExtractor.extract(request, CookieKeys.TOKEN);
        if (token == null || token.isBlank()) {
            throw new InvalidCredentialsException("[ERROR] 로그인이 필요합니다.");
        }

        jwtTokenProvider.validateToken(token);
        LoginMember member = memberService.findMemberByToken(token);

        if (member.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("[ERROR] 접근 권한이 없습니다.");
        }
        return true;
    }
}
