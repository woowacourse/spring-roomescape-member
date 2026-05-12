package roomescape.model;

import java.time.LocalTime;
import roomescape.dto.TimeResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

public record ReservationTime(Long id, LocalTime startAt) {
    public ReservationTime {
        validateStartAt(startAt);
    }

    public static ReservationTime from(TimeResponse timeResponse) {
        return new ReservationTime(timeResponse.id(), timeResponse.startAt());
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt.getMinute() != 0) {
            throw new RoomescapeException(ErrorCode.TIME_WRONG_STARTAT);
        }
    }
}
