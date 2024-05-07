package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        RoomThemeResponse theme) {
    public static ReservationResponse fromReservation(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.fromReservationTime(reservation.getTime()),
                RoomThemeResponse.fromRoomTheme(reservation.getTheme()));
    }
}
