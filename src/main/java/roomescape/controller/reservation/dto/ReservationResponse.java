package roomescape.controller.reservation.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.controller.timeslot.dto.TimeSlotResponse;
import roomescape.model.Reservation;

public record ReservationResponse(
    Long id,
    String name,
    LocalDate date,
    TimeSlotResponse time,
    ThemeResponse theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.id(),
            reservation.name(),
            reservation.date(),
            TimeSlotResponse.from(reservation.timeSlot()),
            ThemeResponse.from(reservation.theme())
        );
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationResponse::from)
            .toList();
    }
}
