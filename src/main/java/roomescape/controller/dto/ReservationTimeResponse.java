package roomescape.controller.dto;

import roomescape.domain.ReservationTime;

public class ReservationTimeResponse {
    private final long id;
    private final String startAt;

    public ReservationTimeResponse(long id, String startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse toDto(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString());
    }

    public long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
