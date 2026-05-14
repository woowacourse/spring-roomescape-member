package roomescape.reservation.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationResult(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        LocalTime startAt,
        Long themeId,
        String themeName
) {

    public static ReservationResult from(Reservation reservation, ReservationTime reservationTime) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTimeId(),
                reservationTime.getStartAt(),
                reservationTime.getTheme().getId(),
                reservationTime.getTheme().getName()
        );
    }
}
