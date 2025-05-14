package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        Long id,
        MemberProfileResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public ReservationResponse(Reservation reservation) {
        this(reservation.getId(),
                new MemberProfileResponse(reservation.getMember()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme()));
    }
}
