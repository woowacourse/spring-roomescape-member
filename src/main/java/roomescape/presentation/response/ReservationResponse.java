package roomescape.presentation.response;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public record ReservationResponse(
    long id,
    UserResponse user,
    LocalDate date,
    TimeSlotResponse time,
    ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
            reservation.id(),
            UserResponse.from(reservation.user()),
            reservation.date(),
            TimeSlotResponse.from(reservation.timeSlot()),
            ThemeResponse.from(reservation.theme())
        );
    }

    public static List<ReservationResponse> from(final List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationResponse::from)
            .toList();
    }
}
