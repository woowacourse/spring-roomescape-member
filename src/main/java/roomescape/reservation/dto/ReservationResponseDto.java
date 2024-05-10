package roomescape.reservation.dto;

import roomescape.auth.dto.MemberResponseDto;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponseDto;
import roomescape.time.dto.ReservationTimeResponseDto;

public record ReservationResponseDto(
        long id,
        MemberResponseDto member,
        String date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {
    public ReservationResponseDto(final Reservation reservation) {
        this(
                reservation.getId(),
                new MemberResponseDto(reservation.getMember().getName()),
                reservation.getReservationDate().getDate().toString(),
                new ReservationTimeResponseDto(reservation.getTime()),
                new ThemeResponseDto(reservation.getTheme())
        );
    }
}
