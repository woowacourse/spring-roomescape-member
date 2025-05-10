package roomescape.auth.web.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;
import roomescape.auth.web.cookie.TokenCookieProvider;

@RequiredArgsConstructor
@Component
public class AdminMemberHandlerInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final TokenCookieProvider tokenCookieProvider;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        Cookie[] cookies = request.getCookies();
        String token = tokenCookieProvider.extractTokenFromCookie(cookies);

        boolean isAdmin = authService.isAdmin(token);
        if (!isAdmin) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
