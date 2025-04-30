package roomescape.theme.repository.entity;

public record ThemeEntity(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    public ThemeEntity(final String name, final String description, final String thumbnail) {
        this(null, name, description, thumbnail);
    }
}
