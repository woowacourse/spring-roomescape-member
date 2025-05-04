package roomescape.reservationTime.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.common.exception.InvalidTimeException;

public record ReservationTimeRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public ReservationTimeRequest {
        if (startAt == null) {
            throw new InvalidTimeException("시간을 입력해주세요.");
        }
    }
}
