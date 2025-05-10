package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.MemberService;
import roomescape.token.Cookies;
import roomescape.token.JwtProvider;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public CheckLoginInterceptor(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String token = Cookies.get(request.getCookies());
        String email = jwtProvider.getTokenSubject(token);
        Member member = memberService.getMemberFrom(email);
        if (member.getRole() != Role.ADMIN) {
            throw new IllegalStateException("권한이 없습니다.");
        }
        return true;
    }
}
