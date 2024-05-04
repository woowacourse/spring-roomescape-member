package roomescape.reservation.dto;

import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.reservation.domain.ReservationTime;

public record TimeCreateRequest(
        @DateTimeFormat(pattern = "HH:mm") LocalTime startAt
) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
