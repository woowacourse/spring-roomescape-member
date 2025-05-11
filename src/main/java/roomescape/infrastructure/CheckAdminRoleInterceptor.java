package roomescape.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.application.MemberService;
import roomescape.application.auth.dto.MemberAuthRequest;
import roomescape.application.dto.MemberDto;
import roomescape.domain.Role;
import roomescape.infrastructure.jwt.MemberAuthRequestExtractor;
import roomescape.application.auth.dto.MemberIdDto;

public class CheckAdminRoleInterceptor implements HandlerInterceptor {

    private final MemberAuthRequestExtractor memberAuthRequestExtractor;
    private final MemberService memberService;

    public CheckAdminRoleInterceptor(MemberAuthRequestExtractor memberAuthRequestExtractor,
                                     MemberService memberService) {
        this.memberAuthRequestExtractor = memberAuthRequestExtractor;
        this.memberService = memberService;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            MemberIdDto memberIdDto = authenticationPrincipalExtractor.extract(request);

            if (member.role() != Role.ADMIN) {
                response.setStatus(401);
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
