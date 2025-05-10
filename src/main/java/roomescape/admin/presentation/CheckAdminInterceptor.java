package roomescape.admin.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.global.exception.NotAdminException;
import roomescape.member.application.service.AuthService;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.AuthorizationExtractor;
import roomescape.member.infrastructure.BearerAuthorizationExtractor;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthorizationExtractor<String> authorizationExtractor;
    private final AuthService authService;

    public CheckAdminInterceptor(AuthService authService) {
        this.authorizationExtractor = new BearerAuthorizationExtractor();
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = authorizationExtractor.extract(request);
        Member member = authService.getUser(token);
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new NotAdminException("접근할 수 없는 페이지입니다.");
        }
        return true;
    }
}
