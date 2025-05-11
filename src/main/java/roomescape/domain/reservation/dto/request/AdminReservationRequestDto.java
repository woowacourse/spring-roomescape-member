package roomescape.domain.reservation.dto.request;

public record AdminReservationRequestDto(String date, Long themeId, Long timeId, Long memberId) {

}
