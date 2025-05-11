package roomescape.presentation.dto.response;

import roomescape.business.model.entity.Reservation;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record ReservationResponse(
        String id,
        UserResponse user,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .sorted(Comparator.comparing(ReservationResponse::date))
                .toList();
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                UserResponse.from(reservation.reserver()),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time()),
                ThemeResponse.from(reservation.theme())
        );
    }

}
