package roomescape.auth.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.domain.AuthInfo;
import roomescape.auth.service.AuthService;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorType;

@Component
@RequiredArgsConstructor
public class CheckMemberInterceptor implements HandlerInterceptor {
    private static final String SESSION_KEY = "token";

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthInfo authInfo = null;
        try {
            String accessToken = extractTokenFromCookie(request.getCookies());
            authInfo = authService.fetchByToken(accessToken);
        } catch (BusinessException | NullPointerException e) {
            throw new BusinessException(ErrorType.SECURITY_EXCEPTION);
        }
        if (authInfo == null) {
            throw new BusinessException(ErrorType.SECURITY_EXCEPTION);
        }
        return true;
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_KEY)) {
                return cookie.getValue();
            }
        }
        throw new BusinessException(ErrorType.SECURITY_EXCEPTION);
    }
}
