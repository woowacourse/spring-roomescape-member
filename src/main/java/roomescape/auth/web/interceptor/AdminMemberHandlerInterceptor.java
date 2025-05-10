package roomescape.auth.web.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.jwt.JwtHandler;

@RequiredArgsConstructor
@Component
public class AdminMemberHandlerInterceptor implements HandlerInterceptor {

    private final JwtHandler jwtHandler;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        boolean isAdmin = jwtHandler.isAdmin(token);
        if (!isAdmin) {
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
