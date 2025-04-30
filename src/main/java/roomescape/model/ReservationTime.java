package roomescape.model;

import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateRequiredFields(id, startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        validateRequiredFields(startAt);

        this.id = null;
        this.startAt = startAt;
    }

    private void validateRequiredFields(Long id, LocalTime startAt) {
        if (id == null) {
            throw new IllegalArgumentException("id는  null 일 수 없습니다.");
        }

        validateRequiredFields(startAt);
    }

    private void validateRequiredFields(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시각은 null 일 수 없습니다.");
        }
    }

    public ReservationTime(Long id) {
        this.id = id;
        this.startAt = null;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
