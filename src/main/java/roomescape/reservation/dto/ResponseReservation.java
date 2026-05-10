package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ResponseTheme;
import roomescape.time.dto.ResponseReservationTime;

import java.time.LocalDate;

public record ResponseReservation(
        Long id,
        String name,
        LocalDate date,
        ResponseReservationTime time,
        ResponseTheme theme
) {

    public static ResponseReservation from(Reservation reservation) {
        return new ResponseReservation(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ResponseReservationTime.from(reservation.getTime()),
                ResponseTheme.from(reservation.getTheme())
        );
    }
}
