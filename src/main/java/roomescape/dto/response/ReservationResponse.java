package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.entity.Reservation;

public record ReservationResponse(Long id, MemberResponse member, LocalDate date,
                                  ReservationTimeResponse time, ThemeResponse theme) {

    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                new MemberResponse(reservation.getMember()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
