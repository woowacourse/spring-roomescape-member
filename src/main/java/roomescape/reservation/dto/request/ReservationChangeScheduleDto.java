package roomescape.reservation.dto.request;

// TODO Dto 검증
public record ReservationChangeScheduleDto(
        String name,
        Long dateId,
        Long timeId
) {
}
