package roomescape.domain;

public class Theme {
    private static final long NO_ID = 0;

    private long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(long id, ThemeName name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(final String name, final String description, final String thumbnail) {
        this(NO_ID, new ThemeName(name), description, thumbnail);
    }

    public Theme(long id, Theme theme) {
        this(id, theme.name, theme.description, theme.thumbnail);
    }

    public Theme(long id, String name, String description, String thumbnail) {
        this(id, new ThemeName(name), description, thumbnail);
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
