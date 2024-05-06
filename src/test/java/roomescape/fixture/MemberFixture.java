package roomescape.fixture;

import roomescape.member.domain.Member;

public class MemberFixture {
    public static Member getMemberChoco() {
        return new Member(1L, "choco");
    }
}
