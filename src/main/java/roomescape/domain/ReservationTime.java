package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private static final long DEFAULT_ID = 0L;

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateNullId(id);
        validateNullStartAt(startAt);
        this.id = id;
        this.startAt = removeNanoSecond(startAt);
    }

    public static ReservationTime createWithoutId(LocalTime time) {
        return new ReservationTime(DEFAULT_ID, time);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }

    private void validateNullId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 ID로 예약 시간을 생성할 수 없습니다.");
        }
    }

    private void validateNullStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 비어있는 시작시간으로 예약 시간을 생성할 수 없습니다.");
        }
    }

    private LocalTime removeNanoSecond(LocalTime time) {
        return time.withNano(0);
    }
}
