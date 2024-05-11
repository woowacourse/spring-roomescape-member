package roomescape.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.TimeSlot;
import roomescape.exception.clienterror.EmptyValueNotAllowedException;

import java.time.LocalTime;


public record TimeSlotRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public TimeSlotRequest {
        isValid(startAt);
    }

    private void isValid(LocalTime startAt) {
        if (startAt == null) {
            throw new EmptyValueNotAllowedException("startAt");
        }
    }

    public TimeSlot toEntity(Long id) {
        return new TimeSlot(id, startAt);
    }
}
