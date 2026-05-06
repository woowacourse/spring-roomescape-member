package roomescape.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public record ReservationResponseDto(
        Long id,
        String name,
        String date,
        String time,
        Theme theme
) {

    public static ReservationResponseDto from(Reservation reservation) {
        String name = reservation.getName().value();
        String date = reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String time = reservation.getTime().getStartAt().format(DateTimeFormatter.ofPattern("HH:mm"));

        return new ReservationResponseDto(
            reservation.getId(),
            name,
            date,
            time,
            reservation.getTheme()
        );
    }
}
