package roomescape.model;

import java.time.LocalTime;
import roomescape.dto.TimeResponse;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime from(TimeResponse timeResponse) {
        return new ReservationTime(timeResponse.id(), timeResponse.startAt());
    }

    public Long id() {
        return id;
    }

    public LocalTime startAt() {
        return startAt;
    }
}
