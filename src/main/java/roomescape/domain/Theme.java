package roomescape.domain;

public class Theme {

    private final Long id;
    private final Name name;
    private final String description;
    private final String thumbnail;

    public Theme(Long id, Name name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this(id, new Name(name), description, thumbnail);
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
