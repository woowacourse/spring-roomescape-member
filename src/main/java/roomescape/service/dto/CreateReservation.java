package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class CreateReservation {

    private final long memberId;
    private final long themeId;
    private final String date;
    private final long timeId;

    public CreateReservation(long memberId, long themeId, String date, long timeId) {
        this.memberId = memberId;
        this.themeId = themeId;
        this.date = date;
        this.timeId = timeId;
    }

    public CreateReservation(LoginMember loginMember, MemberReservationRequest requestDto) {
        this(loginMember.getId(), requestDto.getThemeId(), requestDto.getDate(), requestDto.getTimeId());
    }

    public Reservation toReservation() {
        return new Reservation(
                null,
                Member.memberRole(memberId, null, null, null),
                new Theme(themeId, (String) null, null, null),
                new ReservationDate(date),
                new ReservationTime(timeId, (LocalTime) null)
        );
    }
}
