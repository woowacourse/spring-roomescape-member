package roomescape.reservationtime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record AvailableReservationTimeResponse(Long id,
                                               @JsonFormat(pattern = "HH:mm") LocalTime startAt,
                                               boolean alreadyBooked) {
    public AvailableReservationTimeResponse(final ReservationTime reservationTime, final boolean alreadyBooked) {
        this(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                alreadyBooked
        );
    }
}
