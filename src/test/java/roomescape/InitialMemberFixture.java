package roomescape;

import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.Password;

public class InitialMemberFixture {

    public static final int INITIAL_MEMBER_COUNT = 4;
    public static final Member MEMBER_1 = new Member(
            1L,
            new MemberName("카고"),
            new Email("kargo123@email.com"),
            new Password("123456")
    );
    public static final Member MEMBER_2 = new Member(
            2L,
            new MemberName("브라운"),
            new Email("brown123@email.com"),
            new Password("123456")
    );
    public static final Member MEMBER_3 = new Member(
            3L,
            new MemberName("솔라"),
            new Email("solar123@email.com"),
            new Password("123456")
    );
    public static final Member MEMBER_4 = new Member(
            4L,
            new MemberName("어드민"),
            new Email("admin@email.com"),
            new Password("password")
    );

    public static final Member NOT_SAVED_MEMBER = new Member(
            new MemberName("네오"),
            new Email("neo123@email.com"),
            new Password("123456")
    );
}
