package roomescape.util.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.service.auth.AuthService;

public class RoleCheckInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public RoleCheckInterceptor(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = JwtTokenExtractor.extractTokenFromCookies(request.getCookies());
        Member member = authService.findMemberByToken(token);
        if (member == null || !member.getRole().equals("ADMIN")) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}
