package roomescape.fixture;

import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;

public class MemberFixture {
    public static final Member memberFixture = new Member(1L, "test", "test@gmail.com", "2580", MemberRole.USER);
    public static final Member adminFixture = new Member(2L, "admin", "admin@gmail.com", "0000", MemberRole.ADMIN);
}
