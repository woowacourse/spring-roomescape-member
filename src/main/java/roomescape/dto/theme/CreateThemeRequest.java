package roomescape.dto.theme;

public record CreateThemeRequest(
        String name,
        String description,
        String thumbnailImageUrl
) {
}
