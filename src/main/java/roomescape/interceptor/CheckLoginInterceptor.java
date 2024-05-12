package roomescape.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.MemberInfo;
import roomescape.infrastructure.TokenCookieManager;
import roomescape.service.auth.AuthService;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckLoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String accessToken = TokenCookieManager.getToken(request);
            MemberInfo memberInfo = authService.findMemberByToken(accessToken);

        } catch (Exception e) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
