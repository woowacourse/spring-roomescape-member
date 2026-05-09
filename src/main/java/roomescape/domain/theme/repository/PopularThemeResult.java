package roomescape.domain.theme.repository;

public record PopularThemeResult(
        Long id,
        String name,
        String description,
        String thumbnailUrl,
        int rank
) {
}
