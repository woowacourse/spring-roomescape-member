package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(String startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = parseTime(startAt);
    }

    private LocalTime parseTime(String rawTime) {
        try {
            return LocalTime.parse(rawTime);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("잘못된 시간 형식을 입력하셨습니다.");
        }
    }

    public boolean isBeforeNow() {
        return startAt.isBefore(LocalTime.now());
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
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
        return Objects.hash(id, startAt);
    }
}
