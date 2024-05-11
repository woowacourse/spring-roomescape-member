package roomescape.service.dto.reservation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import roomescape.domain.reservation.ReservationTime;
import roomescape.service.dto.validator.TimeFormatConstraint;

public class ReservationTimeRequest {

    @TimeFormatConstraint
    private final String startAt;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ReservationTimeRequest(String startAt) {
        this.startAt = startAt;
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }

    public String getStartAt() {
        return startAt;
    }
}
