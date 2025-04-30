package roomescape.presentation.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.business.domain.Reservation;

public record ReservationResponse(
        Long id, String name, LocalDate date, LocalTime time
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime().getStartAt()
        );
    }

    public static ReservationResponse withId(final Reservation reservation, final Long id) {
        return new ReservationResponse(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime().getStartAt()
        );
    }
}
