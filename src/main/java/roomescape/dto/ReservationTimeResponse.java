package roomescape.dto;

import roomescape.model.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {

    public static ReservationTimeResponse from(final ReservationTime reservationTime) {
        final String startAt = reservationTime.getFormattedTime();
        return new ReservationTimeResponse(reservationTime.getId(), startAt);
    }
}
