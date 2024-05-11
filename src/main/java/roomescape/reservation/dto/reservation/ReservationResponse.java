package roomescape.reservation.dto.reservation;

import java.time.format.DateTimeFormatter;
import roomescape.member.domain.MemberInfo;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.theme.ThemeResponse;
import roomescape.reservation.dto.time.TimeResponse;

public record ReservationResponse(Long id, String date, TimeResponse time, ThemeResponse theme, MemberInfo member) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(), reservation.getDate().format(DATE_FORMATTER),
                new TimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme()),
                reservation.getMember()
        );
    }
}
