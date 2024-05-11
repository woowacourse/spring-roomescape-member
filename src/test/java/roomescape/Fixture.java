package roomescape;

import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Role;

public class Fixture {
    public static final LoginMember defaultLoginuser = new LoginMember(1L, "name");
    public static final Member defaultMember = new Member(defaultLoginuser, Role.USER, "email@email.com", "password");
}
