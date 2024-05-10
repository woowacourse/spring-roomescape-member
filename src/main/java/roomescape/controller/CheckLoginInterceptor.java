package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.MemberService;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckLoginInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final Cookie[] cookies = request.getCookies();
        final String token = extractTokenFromCookie(cookies);
        if (token.isEmpty()) {
            throw new AuthorizationException("인가 오류"); //TODO 인기가요 아님 주의 인증 오류?
        }

        final Member member = memberService.findMemberByToken(token);

        if (member.getRole() != Role.ADMIN) {
            throw new AuthorizationException("어드민만 올수 있어");
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
