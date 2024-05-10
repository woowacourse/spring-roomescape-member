package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.controller.exception.AuthenticationException;
import roomescape.controller.exception.AuthorizationException;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckAdminInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response, final Object handler) {
        final Cookie[] cookies = request.getCookies();
        final String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) {
            throw new AuthenticationException("인증되지 않은 사용자입니다.");
        }

        final Member member = memberService.findMemberByToken(token);
        if (member.getRole() != Role.ADMIN) {
            throw new AuthorizationException("어드민만 접근할 수 있습니다.");
        }
        return true;
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
