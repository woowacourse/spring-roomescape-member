package roomescape.application.dto.response;

public record AvailableTimeResponse(ReservationTimeResponse time, boolean isBooked) {
}
