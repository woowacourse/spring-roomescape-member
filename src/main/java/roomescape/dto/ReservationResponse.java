package roomescape.dto;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getClientName().getValue(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }
}
