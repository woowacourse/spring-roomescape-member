package roomescape.web.support;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.entity.AccessToken;

@Component
public class CheckAuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();

        Cookie tokenCookie = null;
        if (cookies == null) {
            response.setStatus(401);
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                tokenCookie = cookie;
                break;
            }
        }

        if (tokenCookie == null) {
            //TODO 코드중복 없애기
            response.setStatus(401);
            return false;
        }

        AccessToken accessToken = new AccessToken(tokenCookie.getValue());
        if (!accessToken.isVerified()) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
