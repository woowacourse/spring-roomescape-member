package roomescape.domain;

import java.time.LocalTime;
import roomescape.exception.custom.InvalidInputException;

public class ReservationTime {

    private final long id;
    private final LocalTime startAt;

    public ReservationTime(final long id, final LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(0, startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("시간은 빈 값이 입력될 수 없습니다");
        }
    }

    public ReservationTime withId(final long id) {
        return new ReservationTime(id, this.startAt);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
