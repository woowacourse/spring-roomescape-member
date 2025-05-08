package roomescape.theme.domain;

import roomescape.common.domain.Id;

public class Theme {
    private final Id id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final Id id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme of(final Long id, final String name, final String description, final String thumbnail) {
        return new Theme(Id.from(id), name, description, thumbnail);
    }

    public static Theme withUnassignedId(final String name, final String description, final String thumbnail) {
        return new Theme(Id.unassigned(), name, description, thumbnail);
    }

    public Long getId() {
        return id.getValue();
    }

    public void setId(final Long value) {
        id.setValue(value);
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
