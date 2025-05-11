package roomescape.common.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.service.AuthService;
import roomescape.common.util.TokenUtil;
import roomescape.entity.Member;
import roomescape.entity.Role;
import roomescape.exception.impl.TokenNotFountException;

public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckLoginInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        String token = TokenUtil.extractTokenFromCookie(cookies);
        if (token == null || token.isBlank()) {
            throw new TokenNotFountException();
        }

        Member member = authService.findMemberByToken(token);
        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
