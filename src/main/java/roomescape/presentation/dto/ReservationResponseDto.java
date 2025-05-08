package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.reservation.Reservation;

public record ReservationResponseDto(
        long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time,
        ReservationThemeResponseDto theme
) {

    public static ReservationResponseDto toResponse(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponseDto.toResponse(reservation.getTime()),
                ReservationThemeResponseDto.toResponse(reservation.getTheme())
        );
    }
}
