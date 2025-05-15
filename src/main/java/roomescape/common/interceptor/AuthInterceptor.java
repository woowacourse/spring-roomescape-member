package roomescape.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;
import roomescape.service.AuthService;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    @Autowired
    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Role memberRole = Role.valueOf(
                    authService.getVerifiedPayloadFrom(request.getCookies())
                            .get("role", String.class)
            );
            if (memberRole == Role.ADMIN) {
                return true;
            }

            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;

        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
