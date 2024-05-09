package roomescape.reservation.dto;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(LocalDate date, long timeId, long themeId) {

    public Reservation toReservation(Member member) {
        return new Reservation(null, member, date,
                new ReservationTime(timeId, LocalTime.MIN),
                new Theme(themeId, null, null, null));
    }
}
