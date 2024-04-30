package roomescape.service.dto;

public record ReservationRequest(
        String name,
        String date,
        long timeId) {
}
