package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.exception.UnAuthorizedException;
import roomescape.domain.role.MemberRole;
import roomescape.domain.role.Role;

@Service
public class AuthService {
    private final TokenManager tokenManager;

    public AuthService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public long getMemberId(String token) {
        return tokenManager.extract(token).getMemberId();
    }

    public void validatePermission(String token, Role requiredRole) {
        MemberRole memberRole = tokenManager.extract(token);
        if (!memberRole.hasRoleOf(requiredRole)) {
            throw new UnAuthorizedException();
        }
    }
}
