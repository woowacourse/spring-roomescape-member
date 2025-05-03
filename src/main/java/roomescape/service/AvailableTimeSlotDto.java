package roomescape.service;

import java.time.LocalTime;
import roomescape.model.TimeSlot;

public record AvailableTimeSlotDto(
    Long id,
    LocalTime startAt,
    Boolean alreadyBooked
) {

    public static AvailableTimeSlotDto from(final TimeSlot timeSlot, final boolean alreadyBooked) {
        return new AvailableTimeSlotDto(
            timeSlot.id(),
            timeSlot.startAt(),
            alreadyBooked
        );
    }
}
