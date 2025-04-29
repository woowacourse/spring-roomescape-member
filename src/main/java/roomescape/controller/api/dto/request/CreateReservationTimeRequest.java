package roomescape.controller.api.dto.request;

import java.time.LocalTime;

import roomescape.service.dto.command.CreateReservationTimeCommand;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {

    public CreateReservationTimeCommand toCommand() {
        return new CreateReservationTimeCommand(startAt);
    }
}
