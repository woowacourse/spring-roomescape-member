package roomescape.domain.theme.response;

public record PopularThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        Integer rank
) {
}
