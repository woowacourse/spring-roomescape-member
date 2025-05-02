package roomescape.reservation.dto.response;

public record AvailableTimeResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
}
