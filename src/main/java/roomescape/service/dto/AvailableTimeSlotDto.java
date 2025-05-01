package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.model.TimeSlot;

public record AvailableTimeSlotDto(
    Long id,
    LocalTime startAt,
    Boolean alreadyBooked
) {

    public static AvailableTimeSlotDto from(TimeSlot timeSlot, Boolean alreadyBooked) {
        return new AvailableTimeSlotDto(
            timeSlot.id(),
            timeSlot.startAt(),
            alreadyBooked
        );
    }
}
