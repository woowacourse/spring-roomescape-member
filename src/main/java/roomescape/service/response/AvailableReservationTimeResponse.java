package roomescape.service.response;

public record AvailableReservationTimeResponse(
        Long timeId,
        String startAt,
        boolean alreadyBooked
) {
}
