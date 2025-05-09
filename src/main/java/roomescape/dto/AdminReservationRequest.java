package roomescape.dto;

import java.time.LocalDate;
import roomescape.entity.Member;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;

public record AdminReservationRequest(
        LocalDate date,
        Long themeId,
        Long timeId,
        Long memberId
) {

    public Reservation toReservationWith(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(date, member, reservationTime, theme);
    }
}
