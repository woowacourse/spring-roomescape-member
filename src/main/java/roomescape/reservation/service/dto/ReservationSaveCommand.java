package roomescape.reservation.service.dto;

public record ReservationSaveCommand(
        String name,
        Long dateId,
        Long timeId,
        Long themeId
) {
}
