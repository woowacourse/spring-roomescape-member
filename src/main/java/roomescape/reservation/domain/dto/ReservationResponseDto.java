package roomescape.reservation.domain.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.dto.ReservationTimeResponseDto;
import roomescape.theme.domain.dto.ThemeResponseDto;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time,
        ThemeResponseDto theme
) {
    public static ReservationResponseDto from(Reservation reservation,
                                              ReservationTimeResponseDto reservationTimeResponseDto,
                                              ThemeResponseDto themeResponseDto) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservationTimeResponseDto,
                themeResponseDto
        );
    }
}

