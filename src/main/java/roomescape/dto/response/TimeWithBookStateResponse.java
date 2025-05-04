package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.dto.other.TimeWithBookState;

public record TimeWithBookStateResponse(
        Long id,
        LocalTime startAt,
        Boolean isBooked
) {

    public TimeWithBookStateResponse(TimeWithBookState time) {
        this(time.id(), time.startAt(), time.isBooked());
    }
}
