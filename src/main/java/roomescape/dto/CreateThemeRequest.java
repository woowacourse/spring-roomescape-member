package roomescape.dto;

public record CreateThemeRequest(
        String name,
        String description,
        String thumbnailImageUrl
) {
}
