package roomescape.reservation.domain;

import java.time.LocalTime;

public class ReservationTime {
    private Long id;

    private LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(null, startAt);
    }

    private void validateTime(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 예약 시작 시간을 입력하였습니다.");
        }
    }

    public static ReservationTime of(final long id, final String start_at) {
        return new ReservationTime(id, LocalTime.parse(start_at));
    }

    public ReservationTime assignId(final long id) {
        return new ReservationTime(id, startAt);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
