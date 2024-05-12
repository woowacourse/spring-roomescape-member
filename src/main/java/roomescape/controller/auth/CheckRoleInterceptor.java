package roomescape.controller.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.member.Role;
import roomescape.dto.MemberResponse;
import roomescape.service.MemberService;

@Component
public class CheckRoleInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public CheckRoleInterceptor(final MemberService memberService) {
        this.memberService = memberService;
        this.authorizationExtractor = new AuthorizationExtractor();
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String accessToken = authorizationExtractor.extractToken(request);
        final MemberResponse memberResponse = memberService.findMemberByToken(accessToken);

        if (memberResponse.role() != Role.ADMIN) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        return true;
    }
}
