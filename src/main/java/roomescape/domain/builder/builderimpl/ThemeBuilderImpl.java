package roomescape.domain.builder.builderimpl;

import roomescape.domain.Theme;
import roomescape.domain.builder.ThemeBuilder;

public class ThemeBuilderImpl implements ThemeBuilder {
    private Long id;
    private String name;
    private String description;
    private String thumbnail;

    @Override
    public ThemeBuilder themeId(final long themeId) {
        this.id = themeId;
        return this;
    }

    @Override
    public ThemeBuilder name(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public ThemeBuilder description(final String description) {
        this.description = description;
        return this;
    }

    @Override
    public ThemeBuilder thumbnail(final String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @Override
    public Theme build() {
        return new Theme(id, name, description, thumbnail);
    }
}
