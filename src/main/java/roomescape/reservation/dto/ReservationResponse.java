package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.Time;
import roomescape.time.dto.TimeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(Long id,
                                  String name,
                                  ThemeResponse theme,
                                  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  TimeResponse time) {

    public static ReservationResponse from(Reservation reservation) {
        Theme theme = reservation.theme();
        Time time = reservation.time();
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                new ThemeResponse(theme.id(), theme.name(), theme.description(), theme.thumbnail()),
                reservation.date(),
                new TimeResponse(time.id(), time.startAt()));
    }
}
