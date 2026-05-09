package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.service.dto.ReservationTimeCreateCommand;

public class ReservationTimeRequest {

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startAt;

    public ReservationTimeRequest() {
    }

    public ReservationTimeCreateCommand toCommand() {
        return new ReservationTimeCreateCommand(startAt);
    }
}
