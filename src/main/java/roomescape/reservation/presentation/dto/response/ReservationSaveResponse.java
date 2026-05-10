package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.domain.Reservation;

public record ReservationSaveResponse(
        Long id,
        String name,
        Long scheduleId
) {
    public static ReservationSaveResponse from(Reservation reservation) {
        return new ReservationSaveResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getScheduleId()
        );
    }
}
