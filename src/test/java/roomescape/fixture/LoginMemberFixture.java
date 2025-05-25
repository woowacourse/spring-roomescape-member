package roomescape.fixture;

import roomescape.domain.LoginMember;
import roomescape.domain.Role;

public class LoginMemberFixture {

    private LoginMemberFixture() {
    }

    public static LoginMember getAdmin() {
        return new LoginMember(1L, "어드민", "admin@gmail.com", "wooteco7", Role.ADMIN);
    }

    public static LoginMember getUser() {
        return new LoginMember(2L, "회원", "user@gmail.com", "wooteco7", Role.USER);
    }
}
