package roomescape.fixture;

import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberEmail;
import roomescape.business.domain.member.MemberName;

public class MemberFixture {

    public static final Member MEMBER = new Member(
            1L,
            new MemberName("사용자"),
            new MemberEmail("aaa@gmail.com"),
            "1234"
    );
}
