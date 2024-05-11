package roomescape.domain.reservation;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final ThemeThumbnail thumbnail;

    public Theme(Long id, ThemeName name, ThemeDescription description, ThemeThumbnail thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this(id, new ThemeName(name), new ThemeDescription(description), new ThemeThumbnail(thumbnail));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public String getThumbnail() {
        return thumbnail.getValue();
    }
}
