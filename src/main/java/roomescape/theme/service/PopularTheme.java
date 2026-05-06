package roomescape.theme.service;

public record PopularTheme(
        Long id,
        String name,
        String description,
        String thumbnail,
        int rank
) {
}
