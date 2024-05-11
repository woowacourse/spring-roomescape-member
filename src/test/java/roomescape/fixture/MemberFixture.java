package roomescape.fixture;

import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public class MemberFixture {
    public static final Member DEFAULT_MEMBER = member("prin@gmail.com");

    public static Member member(String email) {
        return new Member(1L, "prin", email, "1q2w3e4r!", Role.MEMBER);
    }
}
