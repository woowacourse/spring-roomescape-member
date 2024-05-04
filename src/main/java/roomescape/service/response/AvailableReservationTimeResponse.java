package roomescape.service.response;

public record AvailableReservationTimeResponse(
        long id,
        String startAt,
        boolean alreadyBooked
) {
}
