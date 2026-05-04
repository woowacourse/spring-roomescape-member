package roomescape.controller.dto;

public record ReservationRequest(
        String name,
        String date,
        Long timeId) {
}
