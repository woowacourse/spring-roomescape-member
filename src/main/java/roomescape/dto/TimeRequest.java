package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidInputException;

public record TimeRequest(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public TimeRequest {
        validateTime(startAt);
    }

    private void validateTime(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("시간을 선택해라.");
        }
    }
}
