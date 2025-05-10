package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.entity.Reservation;

public record ReservationPostResponse(
        Long id,
        LocalDate date,
        MemberSafeResponse member,
        ReservationTimePostResponse time,
        ThemeFullResponse theme
) {
    public ReservationPostResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getDate(),
                new MemberSafeResponse(reservation.getMember()),
                new ReservationTimePostResponse(reservation.getTime()),
                new ThemeFullResponse(reservation.getTheme())
        );
    }
}
