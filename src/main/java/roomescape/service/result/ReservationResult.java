package roomescape.service.result;

import roomescape.domain.Reservation;

import java.time.LocalDate;

public record ReservationResult(
        Long id,
        MemberResult memberResult,
        LocalDate date,
        ReservationTimeResult time,
        ThemeResult theme
) {
    public static ReservationResult from(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                MemberResult.from(reservation.getMember()),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime()),
                ThemeResult.from(reservation.getTheme()));
    }
}
