package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.reservation.Reservation;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                new MemberResponse(reservation.getMember()),
                reservation.getDateTime().getDate(),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
