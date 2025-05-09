package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.MemberRole;
import roomescape.exception.UnAuthorizedException;
import roomescape.service.MemberService;
import roomescape.service.result.MemberResult;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public CheckAdminInterceptor(final CookieProvider cookieProvider, final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.cookieProvider = cookieProvider;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String token = cookieProvider.extractTokenFromCookies(request.getCookies());

        Long id = jwtTokenProvider.extractIdFromToken(token);
        MemberResult memberResult = memberService.findById(id);

        if (memberResult.role() == MemberRole.ADMIN) {
            return true;
        }
        throw new UnAuthorizedException("접근 권한이 필요합니다.");
    }
}
