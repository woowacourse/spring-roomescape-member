package roomescape.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.AccessDeniedException;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.TokenProvider;
import roomescape.auth.config.AuthInfo;

public class AuthenticationInterceptor implements HandlerInterceptor {
    private final TokenProvider tokenProvider;

    public AuthenticationInterceptor(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        String token = extractTokenFromCookie(request.getCookies());
        if (token == null || token.isBlank()) {
            throw new AccessDeniedException("로그인이 필요한 기능입니다. 다시 로그인을 해주세요.");
        }

        AuthInfo authInfo = tokenProvider.extractAuthInfo(token);
        if (authInfo.isNotAdmin()) {
            throw new IllegalAccessException("어드민 권한이 없어 접근이 불가능한 기능입니다.");
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
