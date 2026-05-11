package roomescape.dto.response;

import roomescape.domain.Reservation;

import java.util.List;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        Long themeId
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                ReservationTimeResponse.from(reservation.getTime()),
                reservation.getThemeId()
        );
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
