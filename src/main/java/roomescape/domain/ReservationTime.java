package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateNullStartAt(startAt);
        this.id = id;
        this.startAt = removeNanoSecond(startAt);
    }

    public static ReservationTime createWithoutId(LocalTime time) {
        return new ReservationTime(null, time);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime time = (ReservationTime) o;
        return Objects.equals(id, time.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
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
