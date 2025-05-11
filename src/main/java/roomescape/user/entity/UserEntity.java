package roomescape.user.entity;

import roomescape.user.domain.User;

public record UserEntity(
        Long id,
        String name,
        String email,
        String password
) {
    public User toUser() {
        return User.of(id, name, email);
    }
}
