package roomescape.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        Theme theme
) {

    public static ReservationResponse fromReservation(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getMemberName(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
    }

    public static List<ReservationResponse> fromReservations(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::fromReservation)
                .toList();
    }
}
