package roomescape.time.controller.dto;

import java.time.LocalTime;
import roomescape.time.exception.InvalidTimeRequestException;
import roomescape.time.service.ReservationTimeCommand;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeRequest {
        if (startAt == null) {
            throw new InvalidTimeRequestException();
        }
    }

    public ReservationTimeCommand toCommand() {
        return new ReservationTimeCommand(startAt);
    }
}
