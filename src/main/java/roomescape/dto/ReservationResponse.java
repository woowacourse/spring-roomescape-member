package roomescape.dto;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;

public record ReservationResponse(Long id, String date, ReservationTimeResponse time,
                                  ThemeResponse theme, MemberResponse memberResponse) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate().format(DateTimeFormatter.ISO_DATE),
                ReservationTimeResponse.from(reservation.getReservationTime()),
                ThemeResponse.from(reservation.getTheme()),
                MemberResponse.from(reservation.getMember())
        );
    }
}
