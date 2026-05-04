package roomescape.dto;

import java.time.LocalTime;
import roomescape.model.ReservationTime;

public class TimeResponse {
    private Long id;
    private LocalTime startAt;

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public TimeResponse(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static TimeResponse from(ReservationTime reservationTime) {
        return new TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
