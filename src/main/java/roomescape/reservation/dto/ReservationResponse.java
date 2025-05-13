package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.Time;
import roomescape.time.dto.TimeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.ThemeResponse;

public record ReservationResponse(Long id,
                                  MemberResponse member,
                                  ThemeResponse theme,
                                  @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  TimeResponse time) {

    public static ReservationResponse from(Reservation reservation) {
        Theme theme = reservation.theme();
        Time time = reservation.time();
        return new ReservationResponse(
                reservation.id(),
                MemberResponse.from(reservation.member()),
                ThemeResponse.from(theme),
                reservation.date(),
                TimeResponse.from(time));
    }
}
