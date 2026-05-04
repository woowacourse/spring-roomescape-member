package roomescape.dto;

import roomescape.model.ReservationTime;

import java.time.LocalTime;

public class ReservationTimeResponse {

    private final Long id;
    private final LocalTime time;

    public ReservationTimeResponse(Long id, LocalTime time) {
        this.id = id;
        this.time = time;
    }

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartTime()
        );
    }

    public static ReservationTimeResponse of(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartTime()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return time;
    }

}
