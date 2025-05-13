package roomescape.test.fixture;

import java.time.LocalTime;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class ReservationFixture {

    public static Reservation create(long id, String memberName) {
        Member member = new Member(1L, memberName, "test@test.com", "vadwqe123!", MemberRole.GENERAL);
        ReservationTime time = new ReservationTime(1L, LocalTime.now().minusSeconds(1));
        Theme theme = new Theme(1L, "테마", "설명", "섬네일");
        return new Reservation(id, member, DateFixture.NEXT_DAY, time, theme);
    }
}
