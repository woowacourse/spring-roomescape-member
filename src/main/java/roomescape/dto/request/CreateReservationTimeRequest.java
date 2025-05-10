package roomescape.dto.request;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import roomescape.domain.ReservationTime;

public record CreateReservationTimeRequest(
        @NotNull
        LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
