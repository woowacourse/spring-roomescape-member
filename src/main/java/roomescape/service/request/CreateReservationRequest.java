package roomescape.service.request;

public record CreateReservationRequest(String date, Long memberId, Long timeId, Long themeId) {
}
