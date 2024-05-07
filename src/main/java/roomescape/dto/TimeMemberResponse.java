package roomescape.dto;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeMemberResponse(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {

    public static TimeMemberResponse of(final ReservationTime time, final boolean alreadyBooked) {
        return new TimeMemberResponse(time.getId(), time.getStartAt(), alreadyBooked);
    }
}
