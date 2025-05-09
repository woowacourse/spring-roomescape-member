package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.entity.AccessToken;
import roomescape.entity.MemberRole;

public class CheckAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();

        Cookie tokenCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                tokenCookie = cookie;
                break;
            }
        }

        if (tokenCookie == null) {
            return false;
        }

        AccessToken accessToken = new AccessToken(tokenCookie.getValue());
        if (accessToken.findRole() != MemberRole.ADMIN) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
