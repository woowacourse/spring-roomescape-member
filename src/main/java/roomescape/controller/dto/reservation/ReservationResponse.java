package roomescape.controller.dto.reservation;

import roomescape.controller.dto.reservationTime.ReservationTimeResponse;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getName(),
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
