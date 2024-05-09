package roomescape.fixture;

import roomescape.member.domain.Member;

public class MemberFixture {
    public static Member getMemberChoco() {
        return new Member(1L, "초코칩", "dev.chocochip@gmail.com");
    }

    public static Member getMemberClover(){
        return new Member(2L, "클로버", "dev.clover@gmail.com");
    }
}
