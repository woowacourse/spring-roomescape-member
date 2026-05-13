package roomescape.time;


import java.time.LocalTime;
import roomescape.exception.InvalidStateException;

public class ReservationTime {

    private Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidStateException("예약 시간은 비어있을 수 없습니다.");
        }
    }
}
