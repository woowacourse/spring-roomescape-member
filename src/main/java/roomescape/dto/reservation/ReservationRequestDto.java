package roomescape.dto.reservation;

public record ReservationRequestDto(
        String name,
        String date,
        Long time_id) {
}
