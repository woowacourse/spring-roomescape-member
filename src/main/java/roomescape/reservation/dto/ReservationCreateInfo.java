package roomescape.reservation.dto;

public record ReservationCreateInfo(
        String name,
        String date,
        String time,
        Long themeId
) {
}
