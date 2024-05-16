package roomescape.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.service.AuthService;
import roomescape.service.LoginMemberService;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final LoginMemberService loginMemberService;

    public RoleInterceptor(AuthService authService, LoginMemberService loginMemberService) {
        this.authService = authService;
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        return authService.isMemberAdmin(cookies);
    }
}
