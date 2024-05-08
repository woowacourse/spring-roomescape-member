package roomescape.domain.roomescape;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(final Long id, final ThemeName name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this(null, new ThemeName(name), description, thumbnail);
    }

    public Theme(final long themeId) {
        this(themeId, null, null, null);
    }

    public Theme assignId(final long id) {
        return new Theme(id, name, description, thumbnail);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
