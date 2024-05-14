package roomescape.dto.reservationtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.ReservationTime.ReservationTime;

public record ReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public ReservationTimeRequest {
        Objects.requireNonNull(startAt);
    }

    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
