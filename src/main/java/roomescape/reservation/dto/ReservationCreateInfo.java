package roomescape.reservation.dto;

public record ReservationCreateInfo(
        Long userId,
        String date,
        String time,
        Long themeId
) {
}
