package roomescape.reservation.fixture;

import static roomescape.member.constant.Role.ADMIN;

import roomescape.member.domain.Member;

public class MemberFixture {

    public static final Member MATT = new Member(
            1L, "matt", "matt.kakao", "1234", ADMIN
    );
}
