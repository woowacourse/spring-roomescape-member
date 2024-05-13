package roomescape.domain;

import java.time.LocalTime;
import roomescape.exception.exceptions.InvalidInputException;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("예약 시간이 입력되지 않았습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
