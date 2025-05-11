package roomescape.fixture;

import roomescape.member.domain.Member;
import roomescape.member.domain.MemberEmail;
import roomescape.member.domain.MemberName;

public class MemberFixture {

    public static final Member MEMBER = new Member(
            1L,
            new MemberName("사용자"),
            new MemberEmail("aaa@gmail.com"),
            "1234"
    );
}
