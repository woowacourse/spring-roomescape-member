package roomescape.controller.api.timeslot.dto;

import java.time.LocalTime;
import java.util.List;
import roomescape.model.TimeSlot;

public record TimeSlotResponse(
        Long id,
        LocalTime startAt
) {

    public static TimeSlotResponse from(final TimeSlot timeSlot) {
        return new TimeSlotResponse(
                timeSlot.id(),
                timeSlot.startAt()
        );
    }

    public static List<TimeSlotResponse> from(final List<TimeSlot> timeSlots) {
        return timeSlots.stream()
                .map(TimeSlotResponse::from)
                .toList();
    }
}
