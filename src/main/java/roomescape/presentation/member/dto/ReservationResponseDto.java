package roomescape.presentation.member.dto;

import java.time.LocalDate;
import roomescape.business.domain.reservation.Reservation;
import roomescape.presentation.admin.dto.ReservationThemeResponseDto;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;

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
