package roomescape.dto;

import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponse(long id, String startAt, boolean isAvailable) {
    public static AvailableReservationTimeResponse from(ReservationTime time, boolean isAvailable) {
        return new AvailableReservationTimeResponse(time.getId(), time.getStartAt().toString(), isAvailable);
    }

}
