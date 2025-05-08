package roomescape.service.dto.request;

import java.time.LocalDate;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

public record CreateReservationServiceRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public Reservation toReservation(ReservationTime reservationTime, ReservationTheme reservationTheme) {
        return new Reservation(name, date, reservationTime, reservationTheme);
    }
}
