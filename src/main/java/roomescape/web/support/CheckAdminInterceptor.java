package roomescape.web.support;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.entity.AccessToken;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();

        Cookie tokenCookie = null;
        if (cookies == null) {
            return false;
        }
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
        if (accessToken.isVerified() && accessToken.findMemberRole().isAdmin()) {
            return true;
        }

        response.setStatus(401);
        return false;
    }
}
