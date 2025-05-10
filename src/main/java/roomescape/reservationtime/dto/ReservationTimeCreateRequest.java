package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

public record ReservationTimeCreateRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTimeCreateRequest {
        validateFields(startAt);
    }

    private static void validateFields(LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_TIME);
        }
    }
}
