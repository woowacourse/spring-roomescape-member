package roomescape.reservation.dto;

import roomescape.auth.dto.LoginResponseDto;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.time.dto.ReservationTimeResponseDto;

public record ReservationResponseDto(
        long id,
        LoginResponseDto member,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {
    public ReservationResponseDto(final Reservation reservation) {
        this(
                reservation.getId(),
                new LoginResponseDto(reservation.getMember().getName()),
                reservation.getReservationDate().getDate().toString(),
                new ReservationTimeResponseDto(reservation.getTime()),
                new ThemeResponseDto(reservation.getTheme())
        );
    }
}
