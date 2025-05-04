package roomescape.dto.request;

import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.ReservationTime;

public record ReservationTimeCreateRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(null, startAt);
    }
}
