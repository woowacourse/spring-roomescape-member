package roomescape.domain.theme;

public class Theme {

    private final Long id;
    private final ThemeName name;
    private final Description description;
    private final Thumbnail thumbnail;

    public Theme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = new ThemeName(name);
        this.description = new Description(description);
        this.thumbnail = new Thumbnail(thumbnail);
    }

    public Theme(String name, String description, String thumbnail) {
       this(null, name, description, thumbnail);
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

    public Thumbnail getThumbnail() {
        return thumbnail;
    }
}
