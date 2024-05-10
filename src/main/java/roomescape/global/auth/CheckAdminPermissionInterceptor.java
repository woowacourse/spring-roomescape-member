package roomescape.global.auth;

import static roomescape.domain.member.Role.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberService;
import roomescape.domain.member.Role;
import roomescape.global.exception.AuthorizationException;

@Component
public class CheckAdminPermissionInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public CheckAdminPermissionInterceptor(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("토큰이 존재 하지 않습니다.");
        }
        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        Long memberId = jwtTokenProvider.validateAndGetMemberId(token);
        Member member = memberService.findMemberById(memberId);
        return member.getRole() == ADMIN;
    }
}
