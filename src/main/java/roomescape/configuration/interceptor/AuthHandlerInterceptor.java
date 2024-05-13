package roomescape.configuration.interceptor;

import java.util.Arrays;
import java.util.Objects;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.exception.AuthenticationException;
import roomescape.service.AuthService;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthHandlerInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationException();
        }
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> Objects.equals(cookie.getName(), "token"))
                .findFirst()
                .orElseThrow(AuthenticationException::new)
                .getValue();
        Member member = authService.findByToken(token);
        if (member == null || member.role() != Role.ADMIN) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
