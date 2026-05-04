package roomescape.dto;

import roomescape.domain.Reservation;

import java.time.format.DateTimeFormatter;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time
) {
    public ReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                new ReservationTimeResponse(reservation.getTime())
        );
    }
}
