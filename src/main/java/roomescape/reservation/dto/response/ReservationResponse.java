package roomescape.reservation.dto.response;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeResponse;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse of(Reservation reservation, ReservationTime reservationTime, Theme theme, Member member) {
        return new ReservationResponse(reservation.getId(), member.getName(), reservation.getDate(),
                ReservationTimeResponse.from(reservationTime), ThemeResponse.from(theme)
        );
    }
}
