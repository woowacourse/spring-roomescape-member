package roomescape.dto;

import java.time.LocalTime;

public class ReservationTimeCreationRequest {

    private final LocalTime startAt;

    public ReservationTimeCreationRequest(LocalTime startAt) {
        validateTime(startAt);
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    private void validateTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
        }
    }
}
