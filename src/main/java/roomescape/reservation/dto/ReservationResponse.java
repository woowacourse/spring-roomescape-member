package roomescape.reservation.dto;

import java.time.format.DateTimeFormatter;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.ReservationTimeResponse;

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
