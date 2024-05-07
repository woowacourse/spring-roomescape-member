package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeRequest(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
                                     LocalTime startAt) {

    public ReservationTime toEntity() {
        return new ReservationTime(null, startAt);
    }
}
