package roomescape.Interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

import java.util.Arrays;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthenticationService authenticationService;

    public AdminAuthorizationInterceptor(MemberService memberService, AuthenticationService authenticationService) {
        this.memberService = memberService;
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("token")).findFirst().orElseThrow(() -> new IllegalStateException("토큰이 존재하지 않습니다."));
        String token = cookie.getValue();

        String email = authenticationService.getPayload(token);
        Member member = memberService.findByEmail(email);

        if ("ADMIN".equals(member.getRole())) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
