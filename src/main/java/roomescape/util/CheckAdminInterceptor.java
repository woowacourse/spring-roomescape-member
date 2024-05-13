package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.domain.Role;
import roomescape.member.dto.LoginMember;
import roomescape.member.service.MemberService;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    public CheckAdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractToken(request);
        LoginMember loginMember = memberService.findLoginMemberByToken(token);
        if (!loginMember.role().equals(Role.ADMIN)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                .orElseThrow(() -> new IllegalArgumentException("쿠키가 존재하지 않습니다.")))
                .sequential()
                .filter(cookie -> cookie.getName().equals(JwtTokenProvider.TOKEN))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 존재하지 않습니다."));
    }
}
