package roomescape.fixture;

import roomescape.domain.Member;

public class MemberFixture {

    public static Member getDomain(final String name) {
        return Member.of(null, name, "admin@email.com", "1234");
    }
}
