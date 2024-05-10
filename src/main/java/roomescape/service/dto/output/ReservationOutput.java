package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.Reservation;

public record ReservationOutput(long id, String date, ReservationTimeOutput time, ThemeOutput theme, MemberOutput member) {

    public static ReservationOutput from(final Reservation reservation) {
        return new ReservationOutput(
                reservation.getId(),
                reservation.getDateAsString(),
                ReservationTimeOutput.from(reservation.getTime()),
                ThemeOutput.from(reservation.getTheme()),
                MemberOutput.from(reservation.getMember())
        );
    }

    public static List<ReservationOutput> list(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationOutput::from)
                .toList();
    }
}
