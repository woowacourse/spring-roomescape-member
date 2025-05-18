package roomescape.dto;

import roomescape.model.Role;
import roomescape.model.User;

public record UserResponse(
        Long id,
        String name,
        Role role
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(user.getId(), user.getNameValue(), user.getRole());
    }
}
