package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.reservation.Reservation;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        String themeName
) {
    public static ReservationResponse toDto(Reservation reservation) {
        ReservationTimeResponse dto = ReservationTimeResponse.toDto(reservation.getReservationTime());
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(), dto,
                reservation.getTheme().getName());
    }
}
