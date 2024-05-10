package roomescape.dto.reservation;

import roomescape.domain.reservationtime.ReservationTime;

public record AvailableReservationResponse(String startAt, Long timeId, boolean alreadyBooked, boolean isBeforeNow) {

    public static AvailableReservationResponse of(ReservationTime reservationTime, boolean alreadyBooked, boolean isBeforeNow) {
        return new AvailableReservationResponse(
                reservationTime.getStartAt().toStringTime(),
                reservationTime.getId(),
                alreadyBooked,
                isBeforeNow
        );
    }
}
