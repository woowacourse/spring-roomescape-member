package roomescape.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.service.AuthService;
import roomescape.common.util.CookieUtil;
import roomescape.entity.Member;
import roomescape.entity.Role;
import roomescape.exception.impl.TokenNotFoundException;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    public CheckLoginInterceptor(
            final AuthService authService,
            final JwtTokenProvider jwtTokenProvider
    ) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = CookieUtil.extractTokenFromCookie(request.getCookies());

        if (!validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        Member member = authService.findMemberByToken(token);
        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private boolean validateToken(final String token) {
        if (token == null || token.isBlank()) {
            throw new TokenNotFoundException();
        }
        return jwtTokenProvider.validateToken(token);
    }
}
