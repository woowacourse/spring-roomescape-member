package roomescape.service.dto;

import roomescape.domain.Theme;

public class ThemeRequest {

    private final String name;
    private final String description;
    private final String thumbnail;

    public ThemeRequest(String name, String description, String thumbnail) {
        validateNameExist(name);
        validateDescriptionExist(description);
        validateThumbnailExist(thumbnail);
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }

    private void validateNameExist(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 반드시 입력되어야 합니다.");
        }
    }

    private void validateDescriptionExist(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명은 반드시 입력되어야 합니다.");
        }
    }

    private void validateThumbnailExist(String thumbnail) {
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new IllegalArgumentException("썸네일은 반드시 입력되어야 합니다.");
        }
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
