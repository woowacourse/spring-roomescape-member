package roomescape;

import roomescape.domain.member.Email;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.Password;
import roomescape.domain.member.Role;

public class InitialMemberFixture {

    public static final int INITIAL_MEMBER_COUNT = 4;
    public static final Member MEMBER_1 = new Member(
            1L,
            new MemberName("카고"),
            new Email("kargo123@email.com"),
            new Password("123456"),
            Role.USER
    );
    public static final Member MEMBER_2 = new Member(
            2L,
            new MemberName("브라운"),
            new Email("brown123@email.com"),
            new Password("123456"),
            Role.USER
    );
    public static final Member MEMBER_3 = new Member(
            3L,
            new MemberName("솔라"),
            new Email("solar123@email.com"),
            new Password("123456"),
            Role.USER
    );
    public static final Member MEMBER_4 = new Member(
            4L,
            new MemberName("어드민"),
            new Email("admin@email.com"),
            new Password("password"),
            Role.ADMIN
    );

    public static final Member NOT_SAVED_MEMBER = new Member(
            new MemberName("네오"),
            new Email("neo123@email.com"),
            new Password("123456"),
            Role.USER
    );

    public static final LoginMember LOGIN_MEMBER_1 = new LoginMember(
            MEMBER_1.getId(),
            MEMBER_1.getName(),
            MEMBER_1.getEmail()
    );
    public static final LoginMember LOGIN_MEMBER_2 = new LoginMember(
            MEMBER_2.getId(),
            MEMBER_2.getName(),
            MEMBER_2.getEmail()
    );
    public static final LoginMember LOGIN_MEMBER_3 = new LoginMember(
            MEMBER_3.getId(),
            MEMBER_3.getName(),
            MEMBER_3.getEmail()
    );
    public static final LoginMember LOGIN_MEMBER_4 = new LoginMember(
            MEMBER_4.getId(),
            MEMBER_4.getName(),
            MEMBER_4.getEmail()
    );

    public static final LoginMember NOT_SAVED_LOGIN_MEMBER = new LoginMember(
            NOT_SAVED_MEMBER.getName(),
            NOT_SAVED_MEMBER.getEmail()
    );
}
