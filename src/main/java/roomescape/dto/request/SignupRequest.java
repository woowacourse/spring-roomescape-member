package roomescape.dto.request;

import roomescape.domain.user.User;
import roomescape.domain.user.UserName;

public record SignupRequest(
        String name,
        String email,
        String password
) {
    public User toEntity() {
        return new User(new UserName(name), email, password);
    }
}
