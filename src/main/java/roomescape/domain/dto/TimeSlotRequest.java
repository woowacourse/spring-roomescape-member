package roomescape.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.TimeSlot;

import java.time.LocalTime;

public record TimeSlotRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public TimeSlotRequest {
        isValid(startAt);
    }

    private void isValid(LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 시간입니다");
        }
    }

    public TimeSlot toEntity(Long id) {
        return new TimeSlot(id, startAt);
    }
}
