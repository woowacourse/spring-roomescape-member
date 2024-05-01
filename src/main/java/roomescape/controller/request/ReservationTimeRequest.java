package roomescape.controller.request;

import roomescape.exception.BadRequestException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ReservationTimeRequest {

    private LocalTime startAt;

    public ReservationTimeRequest(String startAt) {
        validate(startAt);
        try {
            this.startAt = LocalTime.parse(startAt);
        } catch (DateTimeParseException exception) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
    }

    private ReservationTimeRequest() {
    }

    private void validate(String startAt) {
        if (startAt == null || startAt.isEmpty()) {
            throw new BadRequestException("[ERROR] 유효하지 않은 요청입니다.");
        }
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
