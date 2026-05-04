package roomescape.dto;

import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
               new ReservationTimeResponse(reservation.getTime().getId(), reservation.getTime().getStartAt()));
    }
}
