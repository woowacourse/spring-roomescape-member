package roomescape.reservation.domain;

public class Theme {
    private static final long NO_ID = 0;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;
    private long id;

    public Theme(long id, ThemeName name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(String name, String description, String thumbnail) {
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
