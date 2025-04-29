package roomescape.reservationtime;

import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime createWithoutId(final LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    private void validateNull(LocalTime startAt) {
        if (startAt == null) {
            throw new NullPointerException("시작 시간은 null일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
