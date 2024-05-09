package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.application.AuthService;
import roomescape.exception.NotAuthenticatedException;
import roomescape.member.domain.Member;

@Component
public class AdminAuthorityInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AdminAuthorityInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = TokenExtractor.extract(request);
        Member member = authService.extractMember(token);
        if (!member.isAdmin()) {
            throw new NotAuthenticatedException("관리자 권한이 없습니다.");
        }
        return true;
    }
}
