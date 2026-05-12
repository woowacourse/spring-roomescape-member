package roomescape.repository.dto;

public record ReservedTheme(
        String id,
        String name,
        String description,
        String imageUrl,
        long reservationCount
) {
}
