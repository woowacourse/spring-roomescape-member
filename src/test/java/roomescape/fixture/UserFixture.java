package roomescape.fixture;

import roomescape.domain.user.User;

public class UserFixture {
    public static final User DEFAULT_USER = user("prin.gmail.com");

    public static User user(String email) {
        return new User(1L, "prin", email, "1q2w3e4r!");
    }
}
