package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime time) {
        this.id = id;
        this.startAt = time;
    }

    public static ReservationTime withId(Long id, ReservationTime reservationTime) {
        return new ReservationTime(
                id,
                reservationTime.startAt
        );
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
