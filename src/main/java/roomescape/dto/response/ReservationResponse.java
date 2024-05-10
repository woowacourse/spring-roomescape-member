package roomescape.dto.response;

import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(
        long id,
        Theme theme,
        Member member,
        LocalDate date,
        ReservationTime time
) {
    public ReservationResponse(final Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getTheme(),
                reservation.getMember(),
                reservation.getDate(),
                reservation.getTime()
        );
    }

    public static List<ReservationResponse> listOf(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
