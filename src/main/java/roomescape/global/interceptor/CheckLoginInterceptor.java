package roomescape.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.JwtProvider;
import roomescape.dto.TokenInfo;
import roomescape.global.util.CookieUtils;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;

    public CheckLoginInterceptor(final JwtProvider jwtProvider, final CookieUtils cookieUtils) {
        this.jwtProvider = jwtProvider;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        String accessToken = cookieUtils.extractToken(request);
        if (accessToken == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        TokenInfo tokenInfo = jwtProvider.verifyTokenAndExtractInfo(accessToken);
        request.setAttribute("memberId", tokenInfo.id());
        return true;
    }
}