package roomescape.fixture;

import static roomescape.domain.member.Role.ADMIN;

import roomescape.domain.member.Member;

public class MemberFixture {

    public static final Member ADMIN_MEMBER = new Member(1L, "어드민", "admin@gmail.com", "123456", ADMIN);
}
