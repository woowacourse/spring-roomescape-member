package roomescape.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.exception.InvalidInputException;

public record TimeRequest(
    @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public TimeRequest {
        validateNull(startAt);
    }

    private void validateNull(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidInputException("선택되지 않은 값이 있거나 입력되지 않은 값이 있습니다.");
        }
    }
}
