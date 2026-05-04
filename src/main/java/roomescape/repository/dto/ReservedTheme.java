package roomescape.repository.dto;

public record ReservedTheme(
        long id,
        String name,
        String description,
        String imageUrl,
        int reservationCount
) {
}
