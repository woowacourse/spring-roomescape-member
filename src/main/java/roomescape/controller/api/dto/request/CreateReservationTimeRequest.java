package roomescape.controller.api.dto.request;

import java.time.LocalTime;

import roomescape.service.dto.command.CreateReservationTimeCommand;

public record CreateReservationTimeRequest(
        LocalTime startAt
) {

    public CreateReservationTimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("시간은 비어있을 수 없습니다.");
        }
    }

    public CreateReservationTimeCommand toCommand() {
        return new CreateReservationTimeCommand(startAt);
    }
}
