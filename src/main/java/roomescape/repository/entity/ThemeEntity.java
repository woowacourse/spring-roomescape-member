package roomescape.repository.entity;

public record ThemeEntity(
        Long id,
        String name,
        String description,
        String thumbnailUrl
) {
}
