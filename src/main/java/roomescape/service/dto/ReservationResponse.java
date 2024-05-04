package roomescape.service.dto;

import roomescape.domain.Reservation;

public record ReservationResponse(
        long id,
        String name,
        String date,
        AllReservationTimeResponse time,
        ThemeResponse theme) {
    public ReservationResponse(final Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(),
                new AllReservationTimeResponse(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme()));
    }
}
