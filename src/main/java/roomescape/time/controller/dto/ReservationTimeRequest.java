package roomescape.time.controller.dto;

import roomescape.time.service.ReservationTimeCommand;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {

    public ReservationTimeCommand toCommand() {
        return new ReservationTimeCommand(startAt);
    }
}
