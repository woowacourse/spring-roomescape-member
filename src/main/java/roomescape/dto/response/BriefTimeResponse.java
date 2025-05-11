package roomescape.dto.response;

import java.time.LocalTime;
import roomescape.model.ReservationTime;

public record BriefTimeResponse(Long id, LocalTime startAt) {

    public static BriefTimeResponse from(ReservationTime time) {
        return new BriefTimeResponse(time.getId(), time.getStartAt());
    }
}
