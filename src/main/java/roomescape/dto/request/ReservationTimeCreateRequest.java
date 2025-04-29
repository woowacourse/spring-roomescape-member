package roomescape.dto.request;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public record ReservationTimeCreateRequest(String startAt) {

    public ReservationTimeCreateRequest {
        validateBlank(startAt);
        validateTimeFormat(startAt);
    }

    public LocalTime getLocalTime() {
        return LocalTime.parse(startAt);
    }


    private void validateBlank(final String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new IllegalArgumentException("빈 값으로 예약할 수 없습니다.");
        }
    }

    private void validateTimeFormat(String startAt) {
        try {
            LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("시간 형식이 올바르지 않습니다. HH:MM 형식으로 입력해주세요.");
        }
    }
}
