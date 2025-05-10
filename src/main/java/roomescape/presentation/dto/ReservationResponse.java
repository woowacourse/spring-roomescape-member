package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;

public record ReservationResponse(
        Long id,
        Member member,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme
) {

    public ReservationResponse(final Reservation reservation, final Member member) {
        this(
                reservation.getId(),
                member,
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
