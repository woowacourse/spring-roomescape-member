package roomescape.member.fixture;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

public class MemberFixture {
    public static final Member MEMBER_ID_1 = new Member(1L, "썬", "aa@gmail.com", "12", Role.MEMBER);
    public static final Member MEMBER_ID_2 = new Member(2L, "리비", "aa@gmail.com", "12", Role.MEMBER);
    public static final Member MEMBER_ID_3 = new Member(3L, "도도", "aa@gmail.com", "12", Role.MEMBER);

}
