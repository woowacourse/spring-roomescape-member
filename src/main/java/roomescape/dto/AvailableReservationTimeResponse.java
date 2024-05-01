package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public record AvailableReservationTimeResponse(
        Long timeId,
        LocalTime startAt,
        boolean alreadyBooked
) {
    public static AvailableReservationTimeResponse of(
            final ReservationTime reservationTime,
            final List<Reservation> reservations
    ) {
        return new AvailableReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                isTimeAvailable(reservations, reservationTime));
    }

    private static boolean isTimeAvailable(final List<Reservation> reservations, final ReservationTime reservationTime) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTime().equals(reservationTime));
    }
}
