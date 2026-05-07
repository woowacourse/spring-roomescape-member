package roomescape.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;
import roomescape.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.dto.theme.ThemeResponseDto;

public record ReservationResponseDto(
        Long id,
        String name,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {

    public static ReservationResponseDto from(Reservation reservation) {
        String name = reservation.getName().value();
        String date = reservation.getDateValue().format(DateTimeFormatter.ISO_LOCAL_DATE);
        ReservationTimeResponseDto time = ReservationTimeResponseDto.from(reservation.getTime());

        return new ReservationResponseDto(
            reservation.getId(),
            name,
            date,
            time,
            ThemeResponseDto.from(reservation.getTheme())
        );
    }
}
