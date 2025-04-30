package roomescape.time.dto;

public record AvailableReservationTimeResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
}
