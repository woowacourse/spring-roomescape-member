package roomescape.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.JwtProvider;
import roomescape.dto.TokenInfo;
import roomescape.global.util.CookieUtils;
import roomescape.model.Role;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;
    private final CookieUtils cookieUtils;

    public CheckAdminInterceptor(final JwtProvider jwtProvider,
                                 final CookieUtils cookieUtils) {
        this.jwtProvider = jwtProvider;
        this.cookieUtils = cookieUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        String accessToken = cookieUtils.extractToken(request);
        if (accessToken == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        TokenInfo tokenInfo = jwtProvider.verifyTokenAndExtractInfo(accessToken);
        if (Role.USER.toString().equals(tokenInfo.role())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
        return true;
    }
}