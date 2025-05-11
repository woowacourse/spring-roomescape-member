package roomescape.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthenticationService;
import roomescape.dto.TokenInfo;
import roomescape.model.Role;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    public CheckAdminInterceptor(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String accessToken = extractTokenFromCookie(cookies);
        if (accessToken == null) {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
        TokenInfo tokenInfo = authenticationService.validateTokenAndGetInfo(accessToken);
        if (tokenInfo.role().equals(Role.USER.toString())) {
            System.out.println(tokenInfo.role());
            System.out.println(Role.USER.toString());
            response.setStatus(401);
            return false;
        }

        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}