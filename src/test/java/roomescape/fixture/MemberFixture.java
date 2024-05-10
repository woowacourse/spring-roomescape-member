package roomescape.fixture;

import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;

public class MemberFixture {
    public static final Member memberFixture = new Member(1L, "테스트", "test@gmail.com", "2580", MemberRole.USER);
    public static final Member adminFixture = new Member(2L, "어드민", "admin@gmail.com", "0000", MemberRole.ADMIN);
}
