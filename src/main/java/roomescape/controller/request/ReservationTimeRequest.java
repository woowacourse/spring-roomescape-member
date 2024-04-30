package roomescape.controller.request;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public record ReservationTimeRequest(String startAt) {
    public ReservationTimeRequest {
        Objects.requireNonNull(startAt);
        validateReservationTime(startAt);
    }

    private void validateReservationTime(String startAt) {
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
