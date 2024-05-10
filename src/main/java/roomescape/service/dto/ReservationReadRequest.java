package roomescape.service.dto;

public record ReservationReadRequest(long themeId, long memberId, String dateFrom, String dateTo) {
}
