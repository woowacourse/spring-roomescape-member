package roomescape.service.dto.response;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(Long id, String name, LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ReservationTimeResponse(reservation.getReservationTime()),
                new ThemeResponse(reservation.getTheme()));

    }

    public static List<ReservationResponse> listOf(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
