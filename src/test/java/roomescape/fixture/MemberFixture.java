package roomescape.fixture;

import roomescape.member.domain.Member;

public class MemberFixture {
    public static Member getMemberChoco() {
        return new Member(1L, "초코칩", "dev.chocochip@gmail.com");
    }
}
