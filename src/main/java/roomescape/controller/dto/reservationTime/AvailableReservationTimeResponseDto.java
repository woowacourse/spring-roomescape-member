package roomescape.controller.dto.reservationTime;

public record AvailableReservationTimeResponseDto (
        Long id,
        String StartAt,
        Boolean available
) {
}
