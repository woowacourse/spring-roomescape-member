package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public class Theme {
    private final ThemeId id;
    private final ThemeName name;
    private final ThemeDescription description;
    private final ThemeThumbnail thumbnail;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme(Long id, String name, String description, String thumbnail) {
        this(
                id == null ? null : new ThemeId(id),
                new ThemeName(name),
                new ThemeDescription(description),
                new ThemeThumbnail(thumbnail)
        );
    }

    private Theme(ThemeId id, ThemeName name, ThemeDescription description, ThemeThumbnail thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Theme withId(Long id) {
        ThemeId themeId = new ThemeId(id);

        if (this.id != null) {
            throw new DomainException(ErrorCode.THEME_ALREADY_HAS_ID);
        }

        return new Theme(themeId, name, description, thumbnail);
    }

    public Long getId() {
        if (id == null) {
            return null;
        }
        return id.value();
    }

    public String getName() {
        return name.value();
    }

    public String getDescription() {
        return description.value();
    }

    public String getThumbnail() {
        return thumbnail.value();
    }
}
