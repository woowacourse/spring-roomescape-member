package roomescape.reservation.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public record AvailableReservationTimeResponse(long id, String startAt, boolean isAvailable) {
    public static AvailableReservationTimeResponse from(ReservationTime time, boolean isAvailable) {
        return new AvailableReservationTimeResponse(time.getId(), time.getStartAt().toString(), isAvailable);
    }

}
