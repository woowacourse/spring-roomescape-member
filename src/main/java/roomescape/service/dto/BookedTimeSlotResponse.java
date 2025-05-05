package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.model.TimeSlot;

public record BookedTimeSlotResponse(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {

    public static BookedTimeSlotResponse from(final TimeSlot timeSlot, final Boolean alreadyBooked) {
        return new BookedTimeSlotResponse(
                timeSlot.id(),
                timeSlot.startAt(),
                alreadyBooked
        );
    }
}
