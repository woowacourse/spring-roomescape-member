package roomescape.theme.dto.request;

import roomescape.theme.model.Theme;

public record CreateThemeRequest(String name, String description, String thumbnail) {

    public CreateThemeRequest(String name, String description, String thumbnail) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("올바른 테마가 아닙니다.");
        }
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme toTheme() {
        return new Theme(null, name, description, thumbnail);
    }
}
