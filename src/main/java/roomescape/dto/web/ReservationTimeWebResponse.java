package roomescape.dto.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.Reservation;

public record ReservationTimeWebResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt) {

    public static ReservationTimeWebResponse from(Reservation reservation) {
        return new ReservationTimeWebResponse(reservation.getTime().getId(), reservation.getTime().getStartAt());
    }
}
