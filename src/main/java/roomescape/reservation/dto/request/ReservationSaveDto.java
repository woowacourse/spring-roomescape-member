package roomescape.reservation.dto.request;

public record ReservationSaveDto(
        String name,
        Long dateId, // todo dateId
        Long timeId,
        Long themeId
) {
}
