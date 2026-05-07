package roomescape.dto.response;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ThemeResponseDto themeResponseDto,
        TimeResponseDto timeDto
) {
    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ThemeResponseDto.from(reservation.getTheme()),
                TimeResponseDto.from(reservation.getTime())
        );
    }
}
