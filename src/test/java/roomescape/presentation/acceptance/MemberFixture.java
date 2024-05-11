package roomescape.presentation.acceptance;

import roomescape.domain.Member;
import roomescape.domain.PlayerName;
import roomescape.domain.Role;

public class MemberFixture {

    public static Member defaultValue() {
        return of("test", "admin@wooteco.com", "1234", Role.ADMIN);
    }

    public static Member of(String name, String email, String password, Role role) {
        return new Member(new PlayerName(name), email, password, role);
    }
}
