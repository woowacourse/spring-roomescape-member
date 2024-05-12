package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.exception.ExceptionType;
import roomescape.exception.RoomescapeException;
import roomescape.service.LoginService;
import roomescape.service.MemberService;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final MemberService memberService;

    public AdminAuthInterceptor(LoginService loginService, MemberService memberService) {
        this.loginService = loginService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = CookieExtractor.getTokenCookie(request).getValue();
        LoginMember loginMember = loginService.checkLogin(token);
        Member findMember = memberService.findById(loginMember.getId());
        if (!findMember.isAdmin()) {
            throw new RoomescapeException(ExceptionType.PERMISSION_DENIED);
        }
        return true;
    }
}
