package roomescape.theme.repository.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ThemeEntity {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public static ThemeEntity of(final Long id, final String name, final String description, final String thumbnail) {
        return new ThemeEntity(id, name, description, thumbnail);
    }
}
