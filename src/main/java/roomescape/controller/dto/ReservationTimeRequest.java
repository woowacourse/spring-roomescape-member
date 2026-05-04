package roomescape.controller.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record ReservationTimeRequest(String startAt) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ReservationTimeRequest {
        validateTime(startAt);
    }

    private void validateTime(String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 시간은 비어 있을 수 없습니다.");
        }

        try {
            LocalTime.parse(startAt, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("[ERROR] 시간 형식이 올바르지 않습니다.");
        }
    }
}
