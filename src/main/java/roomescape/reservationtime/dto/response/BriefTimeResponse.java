package roomescape.reservationtime.dto.response;

import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record BriefTimeResponse(Long id, LocalTime startAt) {

    public static BriefTimeResponse from(ReservationTime time) {
        return new BriefTimeResponse(time.getId(), time.getStartAt());
    }
}
