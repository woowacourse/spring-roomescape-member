package roomescape.model;


import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateNotNull(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validateNotNull(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 null이 될 수 없습니다.");
        }
    }


    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

}
