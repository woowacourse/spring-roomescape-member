package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.controller.exception.InvalidRequestException;
import roomescape.service.dto.ReservationTimeCreateCommand;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {
    public ReservationTimeRequest {
        if (startAt == null) {
            throw new InvalidRequestException("예약 시간은 필수입니다");
        }
    }

    public ReservationTimeCreateCommand toCommand() {
        return new ReservationTimeCreateCommand(startAt);
    }
}
