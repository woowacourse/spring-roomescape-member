package roomescape.time.domain;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationTime(long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    private static final long UNDEFINED = 0;
    public ReservationTime{
        validateStartAt(startAt);
    }
    public ReservationTime(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
        this(UNDEFINED, startAt);
        validateStartAt(startAt);
    }

    public void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
    }
}
