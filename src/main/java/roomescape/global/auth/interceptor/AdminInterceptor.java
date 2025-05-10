package roomescape.global.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.auth.jwt.JwtHandler;
import roomescape.global.exception.impl.ForbiddenException;
import roomescape.global.exception.impl.UnauthorizedException;
import roomescape.login.business.service.TokenCookieService;
import roomescape.member.business.domain.Role;


@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtHandler jwtHandler;
    private final TokenCookieService tokenCookieService;

    public AdminInterceptor(final JwtHandler jwtHandler, final TokenCookieService tokenCookieService) {
        this.jwtHandler = jwtHandler;
        this.tokenCookieService = tokenCookieService;
    }

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        if (request.getCookies() == null) {
            throw new UnauthorizedException("사용자 인증 정보가 없습니다.");
        }

        String accessToken = tokenCookieService.getTokenFromCookies(request.getCookies());
        String roleName = jwtHandler.decode(accessToken, JwtHandler.CLAIM_ROLE_KEY);

        Role role = Role.valueOf(roleName);
        return isAdmin(role);
    }


    private boolean isAdmin(final Role role) {
        if (role == Role.ADMIN) {
            return true;
        }
        throw new ForbiddenException("회원 권한이 존재하지 않아 접근할 수 없습니다.");
    }
}
