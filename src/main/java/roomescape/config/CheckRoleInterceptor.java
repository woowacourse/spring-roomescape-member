package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.dto.MemberProfileInfo;
import roomescape.member.security.service.MemberAuthService;
import roomescape.member.service.MemberLoginService;

@Component
public class CheckRoleInterceptor implements HandlerInterceptor {

    private MemberAuthService memberAuthService;
    private MemberLoginService memberLoginService;

    public CheckRoleInterceptor(MemberAuthService memberAuthService, MemberLoginService memberLoginService) {
        this.memberAuthService = memberAuthService;
        this.memberLoginService = memberLoginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = Objects.requireNonNull(request)
                .getCookies();
        if (memberAuthService.isLoginMember(cookies)) {
            MemberProfileInfo memberProfile = memberAuthService.extractPayload(cookies);
            Member member = memberLoginService.findMemberById(memberProfile.id());
            return member.getRole() == MemberRole.ADMIN;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }

}
