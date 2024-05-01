package roomescape.time.domain;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationTime(long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public ReservationTime(long id, LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        this.id = id;
        this.startAt = startAt;
    }
}
