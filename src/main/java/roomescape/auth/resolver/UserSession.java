package roomescape.auth.resolver;

import roomescape.user.domain.UserId;
import roomescape.user.domain.UserName;
import roomescape.user.domain.UserRole;

public record UserSession(UserId id,
                          UserName name,
                          UserRole role) {
}
