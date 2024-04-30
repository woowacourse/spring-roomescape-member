package roomescape.domain;

public class Theme {
    private long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(final ThemeName name, final String description, final String thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
