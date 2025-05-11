package roomescape.user.application.dto;

import roomescape.user.domain.User;
import roomescape.user.domain.UserId;
import roomescape.user.domain.UserName;
import roomescape.user.domain.UserRole;

public record UserPublicInfoResponse(
        UserId id,
        UserName name,
        UserRole role
) {

    public static UserPublicInfoResponse from(final User user) {
        return new UserPublicInfoResponse(
                user.getId(),
                user.getName(),
                user.getRole()
        );
    }
}
