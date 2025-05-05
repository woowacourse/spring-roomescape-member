package roomescape.presentation.dto.response;

import roomescape.business.model.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        long id,
        UserNameResponse user,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                UserNameResponse.from(reservation.getUser()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

}
