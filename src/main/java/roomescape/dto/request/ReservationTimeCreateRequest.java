package roomescape.dto.request;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.common.exception.InvalidRequestException;
import roomescape.domain.ReservationTime;

public record ReservationTimeCreateRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public ReservationTimeCreateRequest {
        validate(startAt);
    }
    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }
    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }
    }
}
