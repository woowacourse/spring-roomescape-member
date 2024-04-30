package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import roomescape.domain.ReservationTime;

public class ReservationTimeRequestDto {

    private final String startAt;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ReservationTimeRequestDto(String startAt) {
        this.startAt = startAt;
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }

    public String getStartAt() {
        return startAt;
    }
}
