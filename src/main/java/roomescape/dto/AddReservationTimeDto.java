package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record AddReservationTimeDto(@NotNull LocalTime startAt) {

    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}

