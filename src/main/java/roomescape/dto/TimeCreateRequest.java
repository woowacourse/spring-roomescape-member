package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeCreateRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public ReservationTime createReservationTime() {
        return new ReservationTime(startAt);
    }
}
