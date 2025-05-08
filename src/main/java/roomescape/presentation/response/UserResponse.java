package roomescape.presentation.response;

import roomescape.domain.User;

public record UserResponse(
    String name
) {

    public static UserResponse from(final User user) {
        return new UserResponse(
            user.name()
        );
    }
}
