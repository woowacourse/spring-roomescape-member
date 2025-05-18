package roomescape.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.service.LoginService;

public class AdminAuthInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public AdminAuthInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest webRequest, HttpServletResponse response,
        Object handler) throws Exception {
        String token = JwtExtractor.getTokenFromRequest(webRequest);
        Member member = loginService.getLoginMemberByToken(token);
        if (member == null || member.isNotAdmin()) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
