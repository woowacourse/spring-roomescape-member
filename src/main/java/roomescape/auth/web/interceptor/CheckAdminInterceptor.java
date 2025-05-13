package roomescape.auth.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.dto.AuthenticatedMember;
import roomescape.auth.infrastructure.TokenService;
import roomescape.auth.web.support.CookieAuthorizationExtractor;
import roomescape.domain.member.model.Role;
import roomescape.global.exception.AuthorizationException;

@Component
@RequiredArgsConstructor
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieAuthorizationExtractor.extract(request);
        AuthenticatedMember authenticatedMember = tokenService.resolveAuthenticatedMember(token);
        if (Objects.equals(authenticatedMember.role(), Role.ADMIN)) {
            return true;
        }
        throw new AuthorizationException("관리자 권한이 필요합니다.");
    }
}
