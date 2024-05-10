package roomescape.config;

import static roomescape.exception.ExceptionType.REQUIRED_LOGIN;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.ExceptionType;
import roomescape.exception.RoomescapeException;
import roomescape.service.LoginService;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    public AdminAuthInterceptor(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = CookieExtractor.getCookie(request, "token")
                .orElseThrow(() -> new RoomescapeException(REQUIRED_LOGIN))
                .getValue();
        if (!loginService.checkLogin(token).isAdmin()) {
            throw new RoomescapeException(ExceptionType.PERMISSION_DENIED);
        }
        return true;
    }
}
