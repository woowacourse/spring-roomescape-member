package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import roomescape.domain.ReservationTime;

public class ReservationTimeRequest {

    private final String startAt;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ReservationTimeRequest(String startAt) {
        validateTimeExist(startAt);
        validateTimeFormat(startAt);
        this.startAt = startAt;
    }

    private void validateTimeExist(String startAt) {
        if (startAt == null || startAt.isBlank()) {
            throw new IllegalArgumentException("시작 시간은 반드시 입력되어야 합니다.");
        }
    }

    private void validateTimeFormat(String startAt) {
        try {
            LocalTime.parse(startAt);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("시간 형식이 올바르지 않습니다.");
        }
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }

    public String getStartAt() {
        return startAt;
    }
}
