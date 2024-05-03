package roomescape.domain;

import java.util.Objects;

public class RoomTheme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public RoomTheme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public RoomTheme(Long id, RoomTheme roomTheme) {
        this(id, roomTheme.name, roomTheme.description, roomTheme.thumbnail);
    }

    public RoomTheme(Long id, String name, String description, String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public boolean hasId(Long roomThemeId) {
        return Objects.equals(id, roomThemeId);
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
