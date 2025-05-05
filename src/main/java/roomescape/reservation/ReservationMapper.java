package roomescape.reservation;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationRequestDto;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.dto.ThemeResponseDto;

public class ReservationMapper {

    public static Reservation toEntity(ReservationRequestDto requestDto, ReservationTime reservationTime, Theme theme) {
        return Reservation.of(requestDto.name(), requestDto.date(), reservationTime, theme);
    }

    public static ReservationResponseDto toResponseDto(
            Reservation reservation,
            ReservationTimeResponseDto reservationTimeResponseDto,
            ThemeResponseDto themeResponseDto
    ) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservationTimeResponseDto,
                themeResponseDto
        );
    }
}
