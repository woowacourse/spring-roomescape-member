package roomescape.domain.theme.dto;

public record ThemeRequest(
        String name,
        String description,
        String thumbnailImageUrl
) {
}
