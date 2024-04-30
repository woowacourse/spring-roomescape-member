package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ReservationTime {
    private static final int TIME_UNIT = 10;

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(String startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, ReservationTime time) {
        this.id = id;
        this.startAt = time.startAt;
    }

    public ReservationTime(Long id, String startAtInput) {
        LocalTime startAt = convertToLocalTime(startAtInput);
        validateTimeUnit(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(Long id) {
        this.id = id;
        this.startAt = null;
    }

    private void validateTimeUnit(LocalTime time) {
        if (time.getMinute() % TIME_UNIT != 0) {
            throw new IllegalArgumentException("예약 시간은 10분 단위입니다.");
        }
    }

    private LocalTime convertToLocalTime(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("예약 시간이 비어 있습니다.");
        }
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 예약 시간입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startAt);
    }
}
