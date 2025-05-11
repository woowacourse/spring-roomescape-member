package roomescape.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.AuthenticationService;
import roomescape.dto.TokenInfo;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    public CheckLoginInterceptor(final AuthenticationService authenticationService) {
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
        System.out.println("id = " + tokenInfo.id());
        request.setAttribute("memberId", tokenInfo.id());
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