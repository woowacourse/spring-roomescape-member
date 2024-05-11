package roomescape.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;
import roomescape.util.CookieInterpreter;

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
        String token = CookieInterpreter.extractCookie(request);
        String email = authenticationService.getPayload(token);
        Member member = memberService.findByEmail(email);

        if (Role.ADMIN.equals(member.getRole())) {
            return true;
        }
        response.setStatus(401);
        return false;
    }
}
