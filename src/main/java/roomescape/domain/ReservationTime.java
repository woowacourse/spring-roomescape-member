package roomescape.domain;

import java.time.LocalTime;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime withoutId(LocalTime startAt) {
        return new ReservationTime(null, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public String toString() {
        return "ReservationTime{" +
                "id=" + id +
                ", startAt=" + startAt +
                '}';
    }

    private void validateStartTime(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 필수입니다.");
        }
    }
}
