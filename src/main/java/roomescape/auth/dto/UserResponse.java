package roomescape.auth.dto;

import roomescape.user.domain.User;

public record UserResponse(String name) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getName());
    }

}
