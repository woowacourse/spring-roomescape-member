package roomescape.controller.timeslot.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.model.TimeSlot;

public record AddTimeSlotRequest(

        @NotNull(message = "시간은 필수입니다.")
        LocalTime startAt
) {

        public TimeSlot toEntity() {
                return new TimeSlot(startAt);
        }
}
