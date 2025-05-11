package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.AuthService;
import roomescape.application.exception.AuthException;
import roomescape.domain.Role;
import roomescape.presentation.dto.request.LoginMember;

import java.util.Arrays;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthenticationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String token = extractTokenFromCookie(request);

        LoginMember loginMember = authService.findMemberByToken(token);
        if (loginMember == null || loginMember.role() == Role.USER) {
            throw new AuthException("[ERROR] 권한이 필요합니다.", HttpStatus.FORBIDDEN);
        }
        return true;
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthException("[ERROR] 쿠키가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthException("[ERROR] 로그인이 필요합니다.", HttpStatus.UNAUTHORIZED));
    }
}
