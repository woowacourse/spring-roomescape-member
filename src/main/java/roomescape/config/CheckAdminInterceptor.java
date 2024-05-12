package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private static final String TOKEN_KEY = "token";

    private final MemberService memberService;
    private final AuthService authService;

    public CheckAdminInterceptor(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        List<Cookie> tokenCookies = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(TOKEN_KEY))
                .toList();

        if (tokenCookies.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String payload = authService.findPayload(tokenCookies.get(0).getValue());
        Member member = memberService.findAuthInfo(payload);

        if (member.isNotAdmin()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        return true;
    }
}
