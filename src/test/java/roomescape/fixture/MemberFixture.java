package roomescape.fixture;

import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public class MemberFixture {
    public static final Member DEFAULT_MEMBER = new Member(1L, "prin", "prin@gmail.com", "1q2w3e4r!", Role.MEMBER);

    public static final Member DEFAULT_ADMIN = new Member(1L, "prin", "prin@gmail.com", "1q2w3e4r!", Role.ADMIN);
}
