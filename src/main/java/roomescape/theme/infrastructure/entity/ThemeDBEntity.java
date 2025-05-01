package roomescape.theme.infrastructure.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ThemeDBEntity {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    public static ThemeDBEntity of(final Long id, final String name, final String description, final String thumbnail) {
        return new ThemeDBEntity(id, name, description, thumbnail);
    }
}
