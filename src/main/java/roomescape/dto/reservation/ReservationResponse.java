package roomescape.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.time.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(Long id, String name, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  ThemeResponse theme,
                                  ReservationTimeResponse time) {

    public static ReservationResponse from(Reservation reservation, ReservationTime reservationTime, Theme theme) {
        ReservationTimeResponse timeResponseDto = ReservationTimeResponse.from(reservationTime);
        ThemeResponse themeResponse = ThemeResponse.from(theme);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                themeResponse,
                timeResponseDto);
    }
}
