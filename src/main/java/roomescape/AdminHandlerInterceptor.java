package roomescape;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.exception.ForbiddenException;
import roomescape.service.AuthService;

@Component
public class AdminHandlerInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AdminHandlerInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = JwtExtractor.extractFromRequest(request);
        Member member = authService.getMemberByToken(token);
        if (member.getRole() == Role.ADMIN) {
            return true;
        }
        throw new ForbiddenException("어드민만 접근 가능한 페이지입니다.");
    }
}
