package roomescape.dto.response;

import roomescape.domain.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        long id,
        ThemeResponse theme,
        MemberResponse member,
        LocalDate date,
        ReservationTimeResponse time
) {
    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                new ThemeResponse(reservation.getTheme()),
                new MemberResponse(reservation.getMember()),
                reservation.getDate(),
                new ReservationTimeResponse(reservation.getTime())
        );
    }

    public static List<ReservationResponse> listOf(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
