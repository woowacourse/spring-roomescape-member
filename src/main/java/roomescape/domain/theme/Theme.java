package roomescape.domain.theme;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = new ThemeName(name);
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
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
