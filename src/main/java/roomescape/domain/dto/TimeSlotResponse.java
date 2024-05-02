package roomescape.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.TimeSlot;

import java.time.LocalTime;

public record TimeSlotResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public static TimeSlotResponse from(TimeSlot timeSlot) {
        return new TimeSlotResponse(
                timeSlot.getId(),
                timeSlot.getStartAt()
        );
    }
}
