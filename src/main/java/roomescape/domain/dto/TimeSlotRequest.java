package roomescape.domain.dto;

import roomescape.domain.TimeSlot;

import java.time.LocalTime;

public record TimeSlotRequest(LocalTime startAt) {
    public TimeSlot toEntity(Long id) {
        return new TimeSlot(id, startAt);
    }
}
