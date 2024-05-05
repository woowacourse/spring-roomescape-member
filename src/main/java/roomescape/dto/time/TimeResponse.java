package roomescape.dto.time;

import roomescape.domain.ReservationTime;

public record TimeResponse(Long id, String startAt) {

    public TimeResponse(ReservationTime time) {
        this(time.getId(), time.getStartAt());
    }
}
