package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.reservation.Reservation;

public record ReservationResponseDto(
        long id,
        MemberResponseDto member,
        LocalDate date,
        ReservationTimeResponseDto time,
        ReservationThemeResponseDto theme
) {

    public static ReservationResponseDto toResponse(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                MemberResponseDto.toResponse(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResponseDto.toResponse(reservation.getTime()),
                ReservationThemeResponseDto.toResponse(reservation.getTheme())
        );
    }
}
