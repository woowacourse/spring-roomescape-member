package roomescape.domain;

import lombok.Getter;
import roomescape.domain.dto.ThemeCreateData;

@Getter
public class Theme {

    private final Long id;
    private final ThemeName name;
    private final String description;
    private final String thumbnailUrl;

    private Theme(final Long id, final ThemeName name, final String description, final String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static Theme create(final ThemeCreateData data) {
        return new Theme(
                null,
                ThemeName.from(data.name()),
                data.description(),
                data.thumbnailUrl()
        );
    }

    public static Theme createWithId(
            final Long id,
            final String name,
            final String description,
            final String thumbnailUrl
    ) {
        return new Theme(
                id,
                ThemeName.from(name),
                description,
                thumbnailUrl
        );
    }

    public Theme withId(final Long id) {
        return new Theme(
                id,
                name,
                description,
                thumbnailUrl
        );
    }

    public String getName() {
        return name.getName();
    }
}
