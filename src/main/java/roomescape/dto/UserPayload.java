package roomescape.dto;

import roomescape.domain.user.User;

public record UserPayload(
        String id,
        String name,
        String email
) {

    public static UserPayload from(User user) {
        return new UserPayload(
                user.getId().toString(),
                user.getName(),
                user.getEmail()
        );
    }
}
