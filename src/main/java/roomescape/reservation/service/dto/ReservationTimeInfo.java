package roomescape.reservation.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeInfo(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public ReservationTimeInfo(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }
}
