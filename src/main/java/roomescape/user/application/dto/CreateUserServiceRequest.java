package roomescape.user.application.dto;

import roomescape.user.domain.User;
import roomescape.user.domain.UserRole;

public record CreateUserServiceRequest(String email,
                                       String password,
                                       String name) {

    public User toDomain() {
        return User.withoutId(email, password, name, UserRole.MEMBER);
    }
}
