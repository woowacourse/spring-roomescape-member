package roomescape.domain.reservation.entity;

import lombok.Builder;
import lombok.Getter;
import roomescape.common.exception.InvalidArgumentException;

@Getter
public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnail;

    @Builder
    public Theme(final Long id, final String name, final String description, final String thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        validateTheme();
    }

    public static Theme withoutId(final String name, final String description, final String thumbnail) {
        return new Theme(null, name, description, thumbnail);
    }

    public void validateTheme() {
        if (name == null || description == null || thumbnail == null) {
            throw new InvalidArgumentException("Theme field cannot be null");
        }
    }

    public boolean existId() {
        return id != null;
    }
}
