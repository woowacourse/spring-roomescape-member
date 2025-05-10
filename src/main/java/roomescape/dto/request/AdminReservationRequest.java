package roomescape.dto.request;

public record AdminReservationRequest(String date, Long themeId, Long timeId, Long memberId) {
    
}
