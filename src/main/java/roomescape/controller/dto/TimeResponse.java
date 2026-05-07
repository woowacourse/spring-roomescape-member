package roomescape.controller.dto;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.TimeSlot;

public record TimeResponse(long id, LocalTime startAt, boolean isAvailable) {

    public static TimeResponse from(TimeSlot timeSlot) {
        return new TimeResponse(timeSlot.id(), timeSlot.startAt(), true);
    }

    public static List<TimeResponse> availableOf(List<TimeSlot> allTimeSlots, List<Long> reservedTimeId) {
        return allTimeSlots.stream()
                .map(
                        time -> {
                            boolean isAvailable = !reservedTimeId.contains(time.id());
                            return new TimeResponse(time.id(), time.startAt(), isAvailable);
                        }
                ).toList();
    }
}
