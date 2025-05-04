package roomescape.dto;

import java.time.LocalTime;

public record ReservationTimeCreationRequest(LocalTime startAt) {

    public ReservationTimeCreationRequest {
        validateTime(startAt);
    }

    private void validateTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
        }
    }
}
