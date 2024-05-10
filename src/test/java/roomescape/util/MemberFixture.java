package roomescape.util;

import roomescape.member.domain.Member;

public class MemberFixture {
    public static Member getOne() {
        return new Member(null, "name", "email@naver.com", "password");
    }

    public static Member getOneWithId(final Long id) {
        return new Member(id, "name", "email", "password");
    }

    public static Member getOne(final String email) {
        return new Member(null, "name", email, "password");
    }

    public static Member getOne(final String email, final String password) {
        return new Member(null, "name", email, password);
    }
}
