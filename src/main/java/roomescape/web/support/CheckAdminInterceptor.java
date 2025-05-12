package roomescape.web.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.entity.AccessToken;
import roomescape.exception.InvalidTokenException;
import roomescape.web.CookieManager;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;

    public CheckAdminInterceptor(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            String tokenCookie = cookieManager.getCookieByName("token", request);
            AccessToken accessToken = new AccessToken(tokenCookie);
            if (accessToken.isVerified() && accessToken.findMemberRole().isAdmin()) {
                return true;
            }
            throw new InvalidTokenException();
        } catch (InvalidTokenException e) {
            response.setStatus(401);
            return false;
        }
    }
}
