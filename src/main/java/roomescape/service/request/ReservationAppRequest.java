package roomescape.service.request;

public record ReservationAppRequest(String name, String date, Long timeId, Long themeId, Long memberId) {

}
