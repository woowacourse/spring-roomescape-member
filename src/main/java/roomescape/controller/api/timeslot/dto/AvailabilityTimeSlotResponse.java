package roomescape.controller.api.timeslot.dto;

import java.time.LocalTime;
import roomescape.model.TimeSlot;

public record AvailabilityTimeSlotResponse(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {

    public static AvailabilityTimeSlotResponse from(final TimeSlot timeSlot, final Boolean alreadyBooked) {
        return new AvailabilityTimeSlotResponse(
                timeSlot.id(),
                timeSlot.startAt(),
                alreadyBooked
        );
    }
}
