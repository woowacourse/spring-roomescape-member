package roomescape.presentation.dto;

public record ReservationAvailableTimeResponse(String startAt, Long timeId, boolean alreadyBooked) {
}
