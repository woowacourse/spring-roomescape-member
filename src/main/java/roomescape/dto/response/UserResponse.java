package roomescape.dto.response;

import roomescape.domain.User;

public record UserResponse(Long id, String name) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName());
    }
}
