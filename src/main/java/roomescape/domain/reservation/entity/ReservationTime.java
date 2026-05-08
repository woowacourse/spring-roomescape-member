package roomescape.domain.reservation.entity;

import java.time.LocalTime;

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
}
