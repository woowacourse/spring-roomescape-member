package roomescape.model;

import java.time.LocalTime;
import roomescape.dto.TimeResponse;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime from(TimeResponse timeResponse) {
        return new ReservationTime(timeResponse.id(), timeResponse.startAt());
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt.getMinute() != 0) {
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
