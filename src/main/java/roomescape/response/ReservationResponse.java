package roomescape.response;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time,
                                  ThemeResponse theme) {
    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream().map(ReservationResponse::from).toList();
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme())
        );
    }
}
