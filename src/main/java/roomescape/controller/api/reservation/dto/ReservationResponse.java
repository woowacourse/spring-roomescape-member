package roomescape.controller.api.reservation.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.controller.api.member.dto.MemberResponse;
import roomescape.controller.api.theme.dto.ThemeResponse;
import roomescape.controller.api.timeslot.dto.TimeSlotResponse;
import roomescape.model.Reservation;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        TimeSlotResponse time,
        ThemeResponse theme
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                MemberResponse.from(reservation.member()),
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
