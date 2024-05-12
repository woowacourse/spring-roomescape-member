package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.TokenCookieManager;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.MemberInfo;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String accessToken = TokenCookieManager.getToken(request);
        MemberInfo member = authService.findMemberByToken(accessToken);

        if (member.isAdmin()) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}
