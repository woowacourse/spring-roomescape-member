package roomescape.presentation.dto;

import roomescape.business.domain.User;

public record UserResponse(
        Long id,
        String name,
        String email
) {

    public static UserResponse from(final User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
