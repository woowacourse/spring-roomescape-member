package roomescape.support.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberLoginService;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final MemberLoginService memberLoginService;

    public AdminCheckInterceptor(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final String token = memberLoginService.extractToken(request);
        memberLoginService.validateToken(token);
        final MemberResponse memberResponse = memberLoginService.findByToken(token);
        memberResponse.memberRole().validateAdmin();
        return true;
    }
}
