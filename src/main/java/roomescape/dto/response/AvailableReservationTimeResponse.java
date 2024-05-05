package roomescape.dto.response;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record AvailableReservationTimeResponse(Long id, String startAt, boolean alreadyBooked) {
    public AvailableReservationTimeResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this(reservationTime.getId(), reservationTime.getStartAt(DateTimeFormatter.ofPattern("HH:mm")), alreadyBooked);
    }
}
