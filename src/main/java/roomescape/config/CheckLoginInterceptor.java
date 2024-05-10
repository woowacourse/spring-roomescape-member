package roomescape.config;

import static roomescape.controller.MemberController.TOKEN_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.exception.AuthenticationException;
import roomescape.exception.BadRequestException;
import roomescape.service.MemberService;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckLoginInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getCookies() == null) {
            throw new AuthenticationException("인증되지 않은 사용자입니다.");
        }

        String token = extractTokenFromCookie(request.getCookies());

        Member member = memberService.findLoginMember(token);
        if (member == null) {
            throw new BadRequestException("사용자를 찾을 수 없습니다.");
        }

        if (!member.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("접근권한이 없습니다.");
        }

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                return cookie.getValue();
            }
        }

        throw new BadRequestException("올바르지 않은 토큰값입니다.");
    }
}
