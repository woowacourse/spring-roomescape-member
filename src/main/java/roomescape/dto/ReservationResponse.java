package roomescape.dto;

import java.time.LocalDate;
import roomescape.entity.Reservation;

public record ReservationResponse(
        Long id, LocalDate date, MemberResponse member, ReservationTimeResponse time, ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                new MemberResponse(reservation.getMember()),
                new ReservationTimeResponse(reservation.getTime()),
                new ThemeResponse(reservation.getTheme())
        );
    }
}
