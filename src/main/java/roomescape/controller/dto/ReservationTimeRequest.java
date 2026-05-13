package roomescape.controller.dto;

import roomescape.exception.InvalidInputException;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeRequest {
        validateTime(startAt);
    }

    private void validateTime(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("시간은 비어 있을 수 없습니다.");
        }
    }
}
