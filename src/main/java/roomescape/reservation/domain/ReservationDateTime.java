package roomescape.reservation.domain;

import java.time.LocalDateTime;

import roomescape.reservation.request.ReservationRequest;
import roomescape.time.domain.ReservationTime;

public class ReservationDateTime {

    private final LocalDateTime dateTime;

    public ReservationDateTime(ReservationRequest reservationRequest, ReservationTime reservationTime) {
        dateTime = LocalDateTime.of(reservationRequest.date(), reservationTime.startAt());
    }

    public void validatePast(LocalDateTime localDateTime) {
        if (dateTime.isBefore(localDateTime)) {
            throw new IllegalArgumentException("Cannot create a reservation for a past date and time.");
        }
    }
}
