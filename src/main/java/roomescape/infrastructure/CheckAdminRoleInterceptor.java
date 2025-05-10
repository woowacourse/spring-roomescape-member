package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AuthorizationException;

public class CheckAdminRoleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new AuthorizationException("접근 권한이 없습니다.");
        }

        return true;
    }
}
