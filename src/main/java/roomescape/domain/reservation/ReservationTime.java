package roomescape.domain.reservation;

import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(long id, String startAt) {
        this(id, LocalTime.parse(startAt));
    }

    private void validateTime(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 예약 시작 시간을 입력하였습니다.");
        }
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
