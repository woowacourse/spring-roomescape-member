package roomescape.controller.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.entity.Reservation;

public record ReservationResponse(Long id,
                                  String name,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

}
