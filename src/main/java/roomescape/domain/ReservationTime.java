package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime generateWithPrimaryKey(ReservationTime reservationTime, Long newPrimaryKey) {
        return new ReservationTime(newPrimaryKey, reservationTime.startAt);
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    private void validate(LocalTime startAt) {
        // TODO: 시간 관리 예외 처리 만들기
        if (startAt == null) {
            throw new NullPointerException("startAt(시작 시간)은 비어있을 수 없습니다.");
        }
    }
}
