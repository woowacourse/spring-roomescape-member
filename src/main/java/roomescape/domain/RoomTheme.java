package roomescape.domain;

public class RoomTheme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public RoomTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public RoomTheme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public RoomTheme withId(Long id) {
        return new RoomTheme(id, name, description, thumbnail);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
