package roomescape.controller.rest.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.ApplicationException;
import roomescape.global.exception.ExceptionType;
import roomescape.global.util.TokenManager;

public class CheckAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = TokenManager.extractTokenFrom(request.getCookies());
        String role = TokenManager.extractClaim(token, "role");

        if (role.equals("admin")) {
            return true;
        }
        throw new ApplicationException(ExceptionType.FORBIDDEN);
    }
}
