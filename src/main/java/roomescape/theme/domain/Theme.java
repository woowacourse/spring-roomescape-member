package roomescape.theme.domain;

import roomescape.common.domain.Id;

public class Theme {
    private final Id id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public Theme(final Id id, final String name, final String description, final String thumbnail) {
        validateNameLength(name);
        validateDescriptionLength(description);
        validateThumbnailLength(thumbnail);
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

    private void validateNameLength(final String value) {
        if (value.length() > 10) {
            throw new IllegalArgumentException("이름은 10글자 이내여야 합니다.");
        }
    }

    private void validateDescriptionLength(final String value) {
        if (value.length() > 100) {
            throw new IllegalArgumentException("설명은 100글자 이내여야 합니다.");
        }
    }

    private void validateThumbnailLength(final String value) {
        if (value.length() > 100) {
            throw new IllegalArgumentException("썸네일은 100글자 이내여야 합니다.");
        }
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
