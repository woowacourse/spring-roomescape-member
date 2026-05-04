package roomescape.dto;

public record ReservationRequest(
        String name,
        String date,
        Long timeId
) {
}
