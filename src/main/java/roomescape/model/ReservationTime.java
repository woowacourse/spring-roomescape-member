package roomescape.model;

import java.time.LocalTime;

public class ReservationTime {

    private static final int START_MINUTE = 0;

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
        validateStartAt();
    }

    private void validateStartAt() {
        if (startAt.getMinute() != START_MINUTE) {
            throw new IllegalArgumentException("[ERROR] 방탈출의 시작 시간은 정각이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

}
