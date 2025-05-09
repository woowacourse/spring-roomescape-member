package roomescape.user.fixture;

import roomescape.user.domain.dto.User;
import roomescape.user.domain.dto.UserRequestDto;

public class UserFixture {

    public static UserRequestDto createRequestDto(String name, String email, String password) {
        return new UserRequestDto(name, email, password);
    }

    public static User create(String name, String email, String password) {
        return new User(name, email, password);
    }
}
