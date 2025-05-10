package roomescape.support.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.MemberRole;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public AdminCheckInterceptor(JwtTokenProvider jwtTokenProvider, MemberService memberService,
                                 AuthorizationExtractor authorizationExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final String accessToken = authorizationExtractor.extract(request);

        if (accessToken == null) {
            response.setStatus(401);
            return false;
        }

        if (!jwtTokenProvider.validateToken(accessToken)) {
            response.setStatus(401);
            return false;
        }

        final MemberResponse memberResponse = memberService.findByToken(accessToken);

        if (memberResponse == null || !(memberResponse.memberRole() == MemberRole.ADMIN)) {
            response.setStatus(403);
            return false;
        }

        return true;
    }
}
