package roomescape.controller.timeslot.dto;

import java.time.LocalTime;

public record CreateTimeSlotRequest(
    LocalTime startAt
) {

    public CreateTimeSlotRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("모든 값이 존재해야 합니다.");
        }
    }
}
