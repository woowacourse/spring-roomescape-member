package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class ReservationTime {
    private Long id;

    @JsonFormat(pattern = "kk:mm")
    private LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    // TODO ReservationTime은 startAt이 있어야 완전한 객체가 아닐까?
    public ReservationTime(final long id) {
        this(id, null);
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

    public boolean isBefore(final LocalTime now) {
        return startAt.isBefore(now);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
