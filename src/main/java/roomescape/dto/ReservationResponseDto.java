package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain_entity.Reservation;

public record ReservationResponseDto(
        Long id, String name, LocalDate date, ReservationTimeResponseDto time, ThemeResponseDto theme
) {
    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponseDto.from(reservation.getTime()),
                new ThemeResponseDto(reservation.getTheme())
        );
    }
}
