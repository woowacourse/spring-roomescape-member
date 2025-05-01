package roomescape.domain.reservation.dto;

public record BookedReservationTimeResponse(
        ReservationTimeResponse timeResponse,
        boolean alreadyBooked
) {
}
