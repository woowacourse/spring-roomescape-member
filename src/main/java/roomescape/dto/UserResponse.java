package roomescape.dto;

import roomescape.model.User;

public record UserResponse(
        Long id,
        String name
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getNameValue());
    }
}
