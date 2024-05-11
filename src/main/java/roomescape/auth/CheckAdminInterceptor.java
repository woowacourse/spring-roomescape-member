package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.token.TokenManager;
import roomescape.dto.MemberResponse;
import roomescape.exception.AuthenticationException;
import roomescape.exception.AuthorizationException;
import roomescape.model.Role;
import roomescape.service.AuthService;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final TokenManager tokenManager;

    public CheckAdminInterceptor(AuthService authService, TokenManager tokenManager) {
        this.authService = authService;
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String token = tokenManager.extract(request)
                .orElseThrow(() -> new AuthenticationException("토큰 정보가 존재하지 않습니다."));
        final MemberResponse memberResponse = authService.findMemberByToken(token);
        final Role memberRole = Role.from(memberResponse.role());
        if (Role.ADMIN != memberRole) {
            throw new AuthorizationException(String.format("관리자 권한이 없는 사용자입니다. {id: %d, role: %s}", memberResponse.id(), memberResponse.role()));
        }
        return true;
    }
}
