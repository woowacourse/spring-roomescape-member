package roomescape.reservation.dto;

public record ReservationSaveDto(
        String name,
        Long dateId, // todo dateId
        Long timeId,
        Long themeId
) {
}
