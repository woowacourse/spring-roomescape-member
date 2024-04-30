package roomescape.service.dto;

import roomescape.domain.Reservation;

public record ReservationResponse(
        long id,
        String name,
        String date,
        ReservationTimeResponse time) {
    public ReservationResponse(final Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(), new ReservationTimeResponse(reservation.getReservationTime()));
    }
}
