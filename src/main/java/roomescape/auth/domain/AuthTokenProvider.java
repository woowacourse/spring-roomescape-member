package roomescape.auth.domain;

import java.time.Instant;
import roomescape.member.domain.UserRole;

public interface AuthTokenProvider {

    String createAccessToken(String principal, UserRole role);

    String getPrincipal(String token);

    Instant getExpiration(String token);

    UserRole getRole(String token);

    boolean validateToken(String token);
}
