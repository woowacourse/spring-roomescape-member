package roomescape.presentation.auth;

import exception.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import roomescape.application.auth.TokenManager;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

@Component
@RequestScope
public class RequestPayloadContext {
    private final TokenManager tokenManager;
    private MemberRole memberRole;

    public RequestPayloadContext(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public void setMemberRoleIfNotPresent(HttpServletRequest request) {
        if (memberRole != null) {
            return;
        }
        String token = AuthInformationExtractor.extractToken(request);
        memberRole = tokenManager.extract(token);
    }

    public void validatePermission(Role requiredRole) {
        if (memberRole == null || !memberRole.hasRoleOf(requiredRole)) {
            throw new UnAuthorizedException();
        }
    }

    public long getMemberId() {
        if (memberRole == null) {
            throw new IllegalStateException("인증에 오류가 발생했습니다.");
        }
        return memberRole.getMemberId();
    }
}
