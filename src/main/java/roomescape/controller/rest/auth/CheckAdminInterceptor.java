package roomescape.controller.rest.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.ApplicationException;
import roomescape.global.exception.ExceptionType;
import roomescape.global.util.TokenManager;

public class CheckAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ApplicationException(ExceptionType.NO_COOKIE_EXIST);
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> TokenManager.TOKEN_NAME.equals(cookie.getName()))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ExceptionType.NO_TOKEN_EXIST))
                .getValue();

        String role = TokenManager.extractClaim(token, "role");

        if (role.equals("admin")) {
            return true;
        }

        throw new ApplicationException(ExceptionType.FORBIDDEN);
    }
}
