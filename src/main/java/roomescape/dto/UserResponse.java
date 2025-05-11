package roomescape.dto;

import roomescape.model.User;

public record UserResponse(
        String name
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getNameValue());
    }
}
