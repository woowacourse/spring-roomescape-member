package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public record ReservationCreate(
        LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {

    public Reservation toReservationWithoutId(ReservationTime time, Theme theme, Member member) {
        return new Reservation(null, date, time, theme, member);
    }

}

