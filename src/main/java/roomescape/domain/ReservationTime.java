package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime from(Long id, String startAt) {
        try {
            return new ReservationTime(id, LocalTime.parse(startAt));
        } catch (DateTimeException exception) {
            throw new IllegalArgumentException(String.format("%s 는 유효하지 않은 값입니다.(EX: 10:00)", startAt));
        }
    }

    public boolean isBefore(LocalTime other) {
        return this.startAt.isBefore(other);
    }

    public Long getId() {
        return id;
    }

    public String getStartAtAsString() {
        return startAt.toString();
    }
}
