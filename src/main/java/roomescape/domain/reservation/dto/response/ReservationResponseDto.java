package roomescape.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.dto.response.TimeResponseDto;
import roomescape.domain.time.entity.Time;

public record ReservationResponseDto(Long id, String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date, TimeResponseDto time,
                                     ThemeResponseDto theme) {

    public static ReservationResponseDto from(Reservation reservation) {
        Time time = reservation.getTime();
        Theme theme = reservation.getTheme();
        return new ReservationResponseDto(reservation.getId(), reservation.getName(),
            reservation.getDate(), TimeResponseDto.from(time), ThemeResponseDto.from(theme));
    }
}
