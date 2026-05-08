package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveResponse(
        Long id,
        LocalTime startAt
) {
    public static ReservationTimeSaveResponse from(ReservationTime reservationTime) {
        return new ReservationTimeSaveResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
