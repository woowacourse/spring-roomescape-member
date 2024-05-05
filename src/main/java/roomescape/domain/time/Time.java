package roomescape.domain.time;

import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ValidateException;

import java.time.LocalTime;

public class Time {

    private final Long id;
    private final LocalTime startAt;

    public Time(final LocalTime startAt) {
        this(null, startAt);
    }

    public Time(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;

        if (startAt == null) {
            throw new ValidateException(ErrorType.INVALID_ERROR, String.format("유효하지 않은 값입니다.%n%s", this.toString()));
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public String toString() {
        return "Time{" +
                "id=" + id +
                ", startAt=" + startAt +
                '}';
    }
}
