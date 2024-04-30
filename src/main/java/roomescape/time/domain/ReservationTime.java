package roomescape.time.domain;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationTime(long id, LocalTime startAt) {
    @JsonFormat(pattern = "HH:mm")
    public LocalTime startAt() {
        return startAt;
    }
}
