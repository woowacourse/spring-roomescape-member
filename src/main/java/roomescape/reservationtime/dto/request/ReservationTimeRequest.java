package roomescape.reservationtime.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeRequest(
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(
                null,
                startAt
        );
    }
}
