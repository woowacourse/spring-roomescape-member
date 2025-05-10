package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.MemberService;
import roomescape.util.JwtTokenProvider;

public class AuthAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthAdminInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        jwtTokenProvider.validateToken(token);
        Long id = jwtTokenProvider.extractId(token);
        Member memberById = memberService.findMemberById(id);
        Role memberRole = memberById.getRole();
        memberRole.checkAdminAccess();
        return true;
    }
}
