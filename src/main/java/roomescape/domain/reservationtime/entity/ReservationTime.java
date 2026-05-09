package roomescape.domain.reservationtime.entity;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private Long id;

    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateId(id);
        validateStartAt(startAt);

        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime create(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public void assignId(Long id) {
        validateAssignableId(id);
        this.id = id;
    }

    private void validateId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id는 양수여야 합니다.");
        }
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("startAt은 null일 수 없습니다.");
        }
    }

    private void validateAssignableId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }

        validateId(id);

        if (this.id != null) {
            throw new IllegalStateException("이미 id가 할당된 예약 시간입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (this.id == null) {
            return false;
        }
        ReservationTime time = (ReservationTime) other;
        return Objects.equals(this.id, time.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
