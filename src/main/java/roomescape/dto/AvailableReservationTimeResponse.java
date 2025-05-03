package roomescape.dto;

import roomescape.entity.ReservationTime;

import java.time.LocalTime;

public record AvailableReservationTimeResponse(
    Long id,
    LocalTime startAt,
    boolean isBooked
) {

    public static AvailableReservationTimeResponse from(ReservationTime reservationTime, boolean isBooked) {
        return new AvailableReservationTimeResponse(
            reservationTime.getId(),
            reservationTime.getStartAt(),
            isBooked);
    }
}
