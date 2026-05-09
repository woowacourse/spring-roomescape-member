package roomescape.dto.response;

import roomescape.dao.row.ReservationRow;
import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ThemeResponseDto themeResponseDto,
        TimeResponseDto timeDto
) {
    public static ReservationResponseDto from(ReservationRow reservationRow) {
        return new ReservationResponseDto(
                reservationRow.id(),
                reservationRow.name(),
                reservationRow.date(),
                ThemeResponseDto.from(reservationRow.themeRow()),
                TimeResponseDto.from(reservationRow.timeRow())
        );
    }

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName().value(),
                reservation.getDate(),
                ThemeResponseDto.from(reservation.getTheme()),
                TimeResponseDto.from(reservation.getTime())
        );
    }
}
