package roomescape.reservation.dto;

import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.time.dto.ReservationTimeResponseDto;

public record ReservationResponseDto(
        long id,
        String name,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {
    public ReservationResponseDto(final Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getName(),
                reservation.getReservationDate().toString(),
                new ReservationTimeResponseDto(reservation.getTime()),
                new ThemeResponseDto(reservation.getTheme())
        );
    }
}
