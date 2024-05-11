package roomescape.service.request;

public record ReservationRequest(String date, Long memberId, Long timeId, Long themeId) {
}
