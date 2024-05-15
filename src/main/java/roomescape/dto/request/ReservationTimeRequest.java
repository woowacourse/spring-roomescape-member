package roomescape.dto.request;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(
        String startAt
) {
    public ReservationTimeRequest {
        validateReservationTime(startAt);
    }

    private void validateReservationTime(String startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("시간은 null 값 일 수 없습니다.");
        }
        try {
            LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("시간은 HH:mm 형식으로 입력해야 합니다. 입력한 값: " + startAt);
        }
    }

    public ReservationTime toEntity() {
        return new ReservationTime(LocalTime.parse(startAt).withSecond(0));
    }
}
