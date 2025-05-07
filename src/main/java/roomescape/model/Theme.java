package roomescape.model;

public class Theme {
    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, ThemeName name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
