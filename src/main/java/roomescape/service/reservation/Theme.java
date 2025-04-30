package roomescape.service.reservation;

public final class Theme {

    private final Long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final String thumbnail;

    public Theme(final Long id, final ThemeName name, final ThemeDescription description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
