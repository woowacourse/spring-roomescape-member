package roomescape.argumentresolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;
import roomescape.utils.JwtTokenProvider;
import roomescape.web.exception.AuthenticationException;
import roomescape.web.exception.AuthorizationException;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    public CheckAdminInterceptor(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String tokenValue = getTokenValue(cookies);

        String role = tokenProvider.getTokenRole(tokenValue);
        if (Role.ADMIN != Role.valueOf(role)) {
            throw new AuthorizationException("권한이 없습니다.");
        }
        return true;
    }

    private String getTokenValue(Cookie[] cookies) {
        validateCookie(cookies);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthenticationException("로그인된 회원 정보가 없습니다.");
        }
    }
}
