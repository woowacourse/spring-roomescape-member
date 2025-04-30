package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public record ReservationResponseDto(Long id, String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                     ThemeResponseDto theme,
                                     ReservationTimeResponseDto time) {

    public static ReservationResponseDto from(Reservation reservation, ReservationTime reservationTime, Theme theme) {
        ReservationTimeResponseDto timeResponseDto = ReservationTimeResponseDto.from(reservationTime);
        ThemeResponseDto themeResponseDto = ThemeResponseDto.from(theme);

        return new ReservationResponseDto(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                themeResponseDto,
                timeResponseDto);
    }
}
