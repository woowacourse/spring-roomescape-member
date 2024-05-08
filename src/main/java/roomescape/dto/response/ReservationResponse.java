package roomescape.dto.response;

import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {
    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    public static List<ReservationResponse> listOf(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
