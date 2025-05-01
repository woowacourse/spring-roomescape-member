package roomescape.service.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import roomescape.domain.Reservation;

public record ReservationServiceResponse(
        Long id,
        String name,
        LocalDate date,
        LocalTime startAt,
        String themeName
) {

    public static ReservationServiceResponse from(Reservation reservation) {
        return new ReservationServiceResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                reservation.time().startAt(),
                reservation.theme().name()
        );
    }
}
