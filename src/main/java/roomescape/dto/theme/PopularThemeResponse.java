package roomescape.dto.theme;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailImageUrl,
        Long reservedCount
) {
}
