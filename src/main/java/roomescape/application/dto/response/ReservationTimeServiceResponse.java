package roomescape.application.dto.response;

import java.time.LocalTime;

import roomescape.domain.entity.ReservationTime;

public record ReservationTimeServiceResponse(
        Long id,
        LocalTime startAt,
        boolean isBooked
) {

    public static ReservationTimeServiceResponse of(ReservationTime reservationTime, boolean isBooked) {
        return new ReservationTimeServiceResponse(reservationTime.id(), reservationTime.startAt(), isBooked);
    }

    public static ReservationTimeServiceResponse withoutBook(ReservationTime reservationTime) {
        return new ReservationTimeServiceResponse(reservationTime.id(), reservationTime.startAt(), false);
    }
}
