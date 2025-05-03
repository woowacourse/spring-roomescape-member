package roomescape.reservation.dto.response;

public record AvailableReservationTimeResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
}
