package roomescape.admin.domain.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationResponseDto;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.theme.domain.dto.ThemeResponseDto;
import roomescape.user.domain.dto.UserResponseDto;

public record AdminReservationResponseDto(
        Long id,
        LocalDate date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme,
        UserResponseDto user
) {
    public static AdminReservationResponseDto from(Reservation reservation,
                                                   ReservationTimeResponseDto reservationTimeResponseDto,
                                                   ThemeResponseDto themeResponseDto,
                                                   UserResponseDto userResponseDto) {
        return new AdminReservationResponseDto(
                reservation.getId(),
                reservation.getDate(),
                reservationTimeResponseDto,
                themeResponseDto,
                userResponseDto
        );
    }

    public static AdminReservationResponseDto from(ReservationResponseDto reservationResponseDto,
                                                   UserResponseDto userResponseDto) {
        return new AdminReservationResponseDto(
                reservationResponseDto.id(),
                reservationResponseDto.date(),
                reservationResponseDto.time(),
                reservationResponseDto.theme(),
                userResponseDto
        );
    }
}
