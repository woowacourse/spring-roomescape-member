package roomescape.service.dto.output;

import java.util.List;

import roomescape.domain.reservation.Reservation;

public record ReservationOutput(long id, ThemeOutput theme, String date, ReservationTimeOutput time) {

    public static ReservationOutput toOutput(final Reservation reservation) {
        return new ReservationOutput(
                reservation.getId(),
                ThemeOutput.toOutput(reservation.getTheme()),
                reservation.getDate().asString(),
                ReservationTimeOutput.toOutput(reservation.getTime())
        );
    }

    public static List<ReservationOutput> toOutputs(final List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationOutput::toOutput)
                .toList();
    }
}
