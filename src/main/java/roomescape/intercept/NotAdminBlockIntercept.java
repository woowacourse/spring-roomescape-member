package roomescape.intercept;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.exception.BadRequestException;
import roomescape.exception.ForbiddenException;
import roomescape.utility.CookieUtility;
import roomescape.utility.JwtTokenProvider;

public class NotAdminBlockIntercept implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtility cookieUtility;

    public NotAdminBlockIntercept(JwtTokenProvider jwtTokenProvider, CookieUtility cookieUtility) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieUtility = cookieUtility;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        boolean isAdminRequest = request.getRequestURI().startsWith("/admin");
        if (isAdminRequest) {
            String accessToken = getAccessTokenInCookie(request);
            AuthenticationInformation authenticationInformation = jwtTokenProvider.parseToken(accessToken);
            boolean isAdmin = authenticationInformation.isAdmin();
            if (!isAdmin) {
                throw new ForbiddenException("[ERROR] 접근권한이 존재하지 않습니다.");
            }
        }
        return true;
    }

    private String getAccessTokenInCookie(HttpServletRequest request) {
        Optional<Cookie> cookie = cookieUtility.findCookie(request, "access");
        if (cookie.isEmpty()) {
            throw new BadRequestException("[ERROR] 인증 정보가 존재하지 않습니다.");
        }
        return cookie.get().getValue();
    }
}
