package roomescape.reservation.dto;

import roomescape.member.dto.LoginMember;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequestDto(LocalDate date, long timeId, long themeId) {

    public Reservation toReservation(LoginMember member) {
        return new Reservation(null, member.name(), date,
                new ReservationTime(timeId, LocalTime.MIN),
                new Theme(themeId, null, null, null));
    }
}
