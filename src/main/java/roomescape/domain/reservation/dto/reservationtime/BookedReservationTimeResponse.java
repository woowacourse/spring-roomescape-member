package roomescape.domain.reservation.dto.reservationtime;

public record BookedReservationTimeResponse(ReservationTimeResponse timeResponse, boolean alreadyBooked) {
}
