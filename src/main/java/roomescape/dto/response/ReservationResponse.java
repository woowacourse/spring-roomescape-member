package roomescape.dto.response;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        Long themeId
) {
    public static ReservationResponse from(Reservation reservation) {
        ReservationTime reservationTime = reservation.getTime();
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                ReservationTimeResponse.from(reservationTime),
                reservation.getThemeId()
        );
    }

    public static List<ReservationResponse> fromAll(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
