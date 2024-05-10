package roomescape.config.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.config.util.CookieExtractor;
import roomescape.service.AuthService;
import roomescape.service.exception.AuthorizationException;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getCookies() == null) {
            throw new AuthorizationException("사용자 정보를 조회할 수 없습니다.");
        }
        String token = CookieExtractor.extractTokenFromCookie(request.getCookies());
        if (authService.isAdminByToken(token)) {
            return true;
        }
        throw new AuthorizationException("접근할 수 없습니다.");
    }
}
