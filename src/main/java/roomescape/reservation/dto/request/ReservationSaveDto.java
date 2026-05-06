package roomescape.reservation.dto.request;

public record ReservationSaveDto(
        String name,
        Long dateId,
        Long timeId,
        Long themeId
) {
}
