package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final Boolean alreadyBooked;

    public ReservationTime(Long id, LocalTime startAt, Boolean alreadyBooked) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this(id, startAt, null);
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt, null);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간이 null입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public String getStartAt(DateTimeFormatter formatter) {
        return startAt.format(formatter);
    }

    public Boolean getAlreadyBooked() {
        return alreadyBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
