package roomescape.presentation.dto.response;

import roomescape.business.model.entity.User;

import java.util.Comparator;
import java.util.List;

public record UserResponse(
        String id,
        String name,
        String email
) {
    public static UserResponse from(final User user) {
        return new UserResponse(user.id(), user.name(), user.email());
    }

    public static List<UserResponse> from(final List<User> users) {
        return users.stream()
                .map(UserResponse::from)
                .sorted(Comparator.comparing(UserResponse::name))
                .toList();
    }
}
