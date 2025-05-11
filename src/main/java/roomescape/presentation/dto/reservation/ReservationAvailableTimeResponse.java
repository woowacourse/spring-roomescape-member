package roomescape.presentation.dto.reservation;

public record ReservationAvailableTimeResponse(String startAt, Long timeId, boolean alreadyBooked) {
}
