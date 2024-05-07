package roomescape.dto.reservation;

import roomescape.domain.reservationtime.ReservationTime;

public record AvailableReservationResponse(String startAt, Long timeId, boolean alreadyBooked) {

    public static AvailableReservationResponse of(String startAt, Long timeId, boolean alreadyBooked) {
        return new AvailableReservationResponse(startAt, timeId, alreadyBooked);
    }

    public static AvailableReservationResponse of(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationResponse(
                reservationTime.getStartAt().toStringTime(),
                reservationTime.getId(),
                alreadyBooked
        );
    }
}
