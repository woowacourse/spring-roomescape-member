package roomescape.dto.reservationTime;

public record AvailableReservationTimeResponse(
    Long id,
    String StartAt,
    Boolean available
) {
}
