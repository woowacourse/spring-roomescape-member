package roomescape.controller.dto.response;

import java.time.LocalDate;
import roomescape.domain.roomescape.Reservation;

public record ReservationResponse(
        long id,
        MemberNameResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                new MemberNameResponse(reservation.getMember().getNameValue()),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }
}
