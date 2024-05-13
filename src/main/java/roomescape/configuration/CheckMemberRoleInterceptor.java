package roomescape.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;
import roomescape.dto.LoginMember;
import roomescape.exception.exceptions.AuthorizationException;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.service.AuthService;

@Component
public class CheckMemberRoleInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public CheckMemberRoleInterceptor(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = authorizationExtractor.extract(request);
        LoginMember loginMember = authService.findMemberByToken(accessToken);

        if (loginMember.role() != Role.ADMIN) {
            throw new AuthorizationException("권한이 없습니다.");
        }
        return true;
    }
}
