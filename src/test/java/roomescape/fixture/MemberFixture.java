package roomescape.fixture;

import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;

public class MemberFixture {
    public static Member member() {
        return new Member(1L, new MemberName("레모네"), "lemon@wooteco.com", "lemon12", MemberRole.ADMIN);
    }
}
