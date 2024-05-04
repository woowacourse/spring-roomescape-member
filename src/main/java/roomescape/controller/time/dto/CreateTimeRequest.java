package roomescape.controller.time.dto;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

public record CreateTimeRequest(

        @NotNull
        String startAt) {

    public ReservationTime toDomain() {
        return new ReservationTime(startAt);
    }
}
