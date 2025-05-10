package roomescape.user.application.dto;

import roomescape.user.domain.User;
import roomescape.user.domain.UserRole;

public record CreateUserServiceRequest(String name,
                                       String email,
                                       String password) {

    public User toDomain() {
        return User.withoutId(name, email, password, UserRole.NORMAL);
    }
}
