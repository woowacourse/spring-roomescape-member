package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.exception.InvalidInputException;

public record TimeCreateRequest(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public TimeCreateRequest {
        validateTime(startAt);
    }

    private void validateTime(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("시간을 선택해라.");
        }
    }
}
