package roomescape.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.controller.exception.AccessDeniedException;
import roomescape.controller.exception.UnauthorizedException;
import roomescape.domain.member.Role;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;
import roomescape.util.CookieUtil;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = CookieUtil.extractTokenFromCookie(request);

        if (token == null) {
            throw new UnauthorizedException();
        }

        Long memberId = jwtTokenProvider.getMemberId(token);
        MemberResponse memberResponse = memberService.getById(memberId);

        if (memberResponse.role() != Role.ADMIN) {
            throw new AccessDeniedException("어드민 권한이 필요합니다.");
        }

        return true;
    }
}
