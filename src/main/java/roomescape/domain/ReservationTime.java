package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    private ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(LocalTime startAt) {
        validateStartTime(startAt);
        return new ReservationTime(null, startAt);
    }

    public static ReservationTime of(Long id, LocalTime startAt) {
        validateId(id);
        validateStartTime(startAt);
        return new ReservationTime(id, startAt);
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID는 필수값입니다.");
        }
        if (id < 1) {
            throw new IllegalArgumentException("ID는 1 이상의 숫자여야 합니다. (입력값: " + id + ")");
        }
    }

    private static void validateStartTime(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작시간은 필수입니다.");
        }
        if (startAt.getMinute() % 60 != 0) {
            throw new IllegalArgumentException("예약 시간은 한 시간 단위로만 설정 가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReservationTime{" +
                "id=" + id +
                ", startAt=" + startAt +
                '}';
    }
}
