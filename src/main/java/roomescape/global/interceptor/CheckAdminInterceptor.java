package roomescape.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.JwtProvider;
import roomescape.dto.TokenInfo;
import roomescape.model.Role;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;

    public CheckAdminInterceptor(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = extractTokenFromCookie(cookies);
        if (accessToken == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        TokenInfo tokenInfo = jwtProvider.validateTokenAndGetInfo(accessToken);
        if (tokenInfo.role().equals(Role.USER.toString())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}