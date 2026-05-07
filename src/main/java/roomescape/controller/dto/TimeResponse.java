package roomescape.controller.dto;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.TimeSlot;
import roomescape.service.dto.AvailableTimeSlot;

public record TimeResponse(long id, LocalTime startAt, boolean isAvailable) {

    public static TimeResponse from(TimeSlot timeSlot) {
        return new TimeResponse(timeSlot.id(), timeSlot.startAt(), true);
    }

    public static TimeResponse from(AvailableTimeSlot availableTimeSlot) {
        return new TimeResponse(
                availableTimeSlot.timeSlot().id(),
                availableTimeSlot.timeSlot().startAt(),
                availableTimeSlot.isAvailable()
        );
    }
}
