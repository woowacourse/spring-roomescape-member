package roomescape.controller.dto.reservation;

import roomescape.domain.Reservation;
import roomescape.controller.dto.reservationTime.ReservationTimeResponseDto;
import roomescape.controller.dto.theme.ThemeResponseDto;

import java.time.format.DateTimeFormatter;

public record ReservationResponseDto(
        Long id,
        String name,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {

    public static ReservationResponseDto from(Reservation reservation) {
        String name = reservation.getName().value();
        String date = reservation.getDate().value().format(DateTimeFormatter.ISO_LOCAL_DATE);

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
