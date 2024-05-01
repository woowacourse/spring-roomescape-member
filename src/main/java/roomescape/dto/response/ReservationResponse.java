package roomescape.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        String name,
        String date,
        ReservationTimeResponse time,
        RoomThemeResponse theme) {
    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(),
                reservation.getName(),
                reservation.getDate().format(DateTimeFormatter.ISO_DATE),
                new ReservationTimeResponse(reservation.getTime()), //TODO 정팩매 사용
                RoomThemeResponse.fromRoomTheme(reservation.getTheme()));
    }
}
