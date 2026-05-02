package roomescape.service.dto.response;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResult(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResult time
) {

    public static ReservationResult from(final Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime())
        );
    }
}
