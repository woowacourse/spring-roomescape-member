package roomescape.config.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Member;
import roomescape.service.AuthService;
import roomescape.service.exception.AuthorizationException;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public CheckLoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = authService.extractTokenFromCookie(request.getCookies());
        Member member = authService.findMemberByToken(token);
        if (member.isNotMember()) {
            throw new AuthorizationException("로그인이 필요합니다.");
        }
        return true;
    }
}
