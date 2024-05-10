package roomescape.config;

import java.util.Arrays;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.CustomUnauthorized;
import roomescape.member.service.AuthService;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("token"))
                    .map(authService::parseEmail)
                    .filter(authService::checkAdmin)
                    .findFirst()
                    .orElseThrow(() -> new CustomException(CustomUnauthorized.NOT_AUTHORIZED));
            return true;
        } catch (NullPointerException e) {
            response.sendRedirect("/login");
        }
        return false;
    }
}
