package roomescape.reservation.dto.response;

import roomescape.reservation.Reservation;

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
