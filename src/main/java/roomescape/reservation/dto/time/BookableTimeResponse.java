package roomescape.reservation.dto.time;

import roomescape.reservation.domain.ReservationTime;

public record BookableTimeResponse(Long id, String startAt, boolean booked) {

    public BookableTimeResponse(ReservationTime time, boolean booked) {
        this(time.getId(), time.getStartAt().toString(), booked);
    }
}
