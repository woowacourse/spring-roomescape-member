package roomescape.controller.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeRequest {
        validateTime(startAt);
    }

    private void validateTime(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 비어 있을 수 없습니다.");
        }
    }
}
