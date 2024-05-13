package roomescape.dto.reservation;

import java.time.LocalDate;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.dto.theme.ThemeResponse;

public record ReservationResponse(
        long id,
        String name,
        ThemeResponse theme,
        LocalDate date,
        ReservationTimeResponse time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getMember().getMemberName().getValue(),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTimeId(), reservation.getTime()));
    }
}
