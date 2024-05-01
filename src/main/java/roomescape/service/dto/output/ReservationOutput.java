package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.Reservation;

public record ReservationOutput(long id, String name, ThemeOutput theme, String date, ReservationTimeOutput time) {

    public static ReservationOutput toOutput(Reservation reservation) {
        return new ReservationOutput(
                reservation.getId(),
                reservation.getNameAsString(),
                ThemeOutput.toOutput(reservation.getTheme()),
                reservation.getDate().asString(),
                ReservationTimeOutput.toOutput(reservation.getTime())
        );
    }

    public static List<ReservationOutput> toOutputs(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationOutput::toOutput)
                .toList();
    }
}
