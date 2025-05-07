package roomescape.domain;

import roomescape.exception.theme.ThemeFieldRequiredException;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    private Theme(Long id, String name, String description, String thumbnail) {
        validate(name, description, thumbnail);
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public static Theme createWithoutId(String name, String description, String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    public static Theme createWithId(Long id, String name, String description, String thumbnail) {
        return new Theme(id, name, description, thumbnail);
    }

    private void validate(String name, String description, String thumbnail) {
        validateName(name);
        validateDescription(description);
        validateThumbnail(thumbnail);
    }

    private void validateThumbnail(String thumbnail) {
        if (thumbnail.isBlank()) {
            throw new ThemeFieldRequiredException("썸네일");
        }
    }

    private void validateDescription(String description) {
        if (description.isBlank()) {
            throw new ThemeFieldRequiredException("설명");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new ThemeFieldRequiredException("이름");
        }
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
