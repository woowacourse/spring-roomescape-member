package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.Reservation;

public record ReservationOutput(long id, String name, ThemeOutput theme, String date, ReservationTimeOutput time) {

    public static ReservationOutput from(final Reservation reservation) {
        return new ReservationOutput(
                reservation.getId(),
                reservation.getNameAsString(),
                ThemeOutput.from(reservation.getTheme()),
                reservation.getDate().asString(),
                ReservationTimeOutput.from(reservation.getTime())
        );
    }

    public static List<ReservationOutput> list(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationOutput::from)
                .toList();
    }
}
