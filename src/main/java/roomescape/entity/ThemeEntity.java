package roomescape.entity;

import roomescape.domain.Theme;

public record ThemeEntity(Long id, String name, String description, String thumbnail) {
    public static ThemeEntity of(Long id, Theme theme) {
        return new ThemeEntity(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public Theme toDomain() {
        Theme theme = new Theme(this.name, this.description, this.thumbnail);
        return theme.withId(this.id);
    }
}
