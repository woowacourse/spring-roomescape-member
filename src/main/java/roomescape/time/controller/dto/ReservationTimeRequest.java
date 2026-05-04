package roomescape.time.controller.dto;

import java.time.LocalTime;
import roomescape.time.service.ReservationTimeCommand;

import java.util.regex.Pattern;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeCommand toCommand() {
        return new ReservationTimeCommand(startAt);
    }
}
