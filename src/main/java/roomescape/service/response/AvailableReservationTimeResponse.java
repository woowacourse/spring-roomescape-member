package roomescape.service.response;

public record AvailableReservationTimeResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
}
