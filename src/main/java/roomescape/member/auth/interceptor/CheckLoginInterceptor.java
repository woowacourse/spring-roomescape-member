package roomescape.member.auth.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.service.MemberService;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckLoginInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies)
                .orElseThrow(() -> new IllegalArgumentException("token 쿠키가 없습니다."));
        Member member = memberService.searchLoginMember(token);

        if (member == null || !member.isSameRole(Role.ADMIN)) {
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
