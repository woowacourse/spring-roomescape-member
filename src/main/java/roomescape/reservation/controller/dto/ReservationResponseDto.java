package roomescape.reservation.controller.dto;

import java.time.LocalDate;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.controller.dto.ThemeResponseDto;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponseDto.from(reservation.getTime()),
                ThemeResponseDto.from(reservation.getTheme())
        );
    }
}
