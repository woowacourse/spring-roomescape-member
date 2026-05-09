package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.service.dto.ReservationTimeCreateCommand;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {
    public ReservationTimeCreateCommand toCommand() {
        return new ReservationTimeCreateCommand(startAt);
    }
}
