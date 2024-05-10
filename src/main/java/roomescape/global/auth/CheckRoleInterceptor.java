package roomescape.global.auth;

import static roomescape.domain.member.Role.ADMIN;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.LoginMember;
import roomescape.global.exception.AuthorizationException;

@Component
public class CheckRoleInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public CheckRoleInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthorizationException("잘못된 접근입니다.");
        }
        String token = jwtTokenProvider.extractTokenFromCookies(cookies);
        if (token.isEmpty()) {
            throw new AuthorizationException("잘못된 접근입니다.");
        }
        LoginMember member = jwtTokenProvider.parse(token);
        if (member.role() != ADMIN) {
            throw new AuthorizationException("잘못된 접근입니다.");
        }
        return true;
    }
}
