package roomescape.service.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public static ReservationTimeResponse of(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
