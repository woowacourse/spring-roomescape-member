package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.TimeResponse;

public record ReservationResponse(Long id,
                                  String name,
                                  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  TimeResponse time, ThemeResponse theme) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()));
    }
}
