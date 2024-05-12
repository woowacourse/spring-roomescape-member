package roomescape.domain;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final Description description;
    private final String thumbnail;

    public Theme(ThemeName name, Description description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, ThemeName name, Description description, String thumbnail) {
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

    public Description getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
