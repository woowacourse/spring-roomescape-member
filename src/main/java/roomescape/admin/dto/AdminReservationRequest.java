package roomescape.admin.dto;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

public record AdminReservationRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {

    public Reservation toReservation() {
        return new Reservation(null,
                new Member(memberId, null, null, null, null),
                date,
                new ReservationTime(timeId, LocalTime.MIN),
                new Theme(themeId, null, null, null));
    }
}
