package roomescape.domain;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(ThemeName name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, ThemeName name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public ThemeName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
