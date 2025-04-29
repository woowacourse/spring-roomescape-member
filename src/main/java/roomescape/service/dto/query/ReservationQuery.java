package roomescape.service.dto.query;

import java.time.LocalDate;
import java.time.LocalTime;

import roomescape.domain.Reservation;

public record ReservationQuery(
        Long id,
        String name,
        LocalDate date,
        Long timeId,
        LocalTime startAt
) {

    public static ReservationQuery from(Reservation reservation) {
        return new ReservationQuery(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                reservation.time().getId(),
                reservation.time().getStartAt()
        );
    }
}
