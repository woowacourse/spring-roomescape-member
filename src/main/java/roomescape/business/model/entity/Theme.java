package roomescape.business.model.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;
import roomescape.business.model.vo.Id;
import roomescape.business.model.vo.ThemeName;

import java.util.Objects;

@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Theme {

    private final Id id;
    private final ThemeName name;
    private final String description;
    private final String thumbnail;

    public static Theme create(final String name, final String description, final String thumbnail) {
        return new Theme(Id.issue(), new ThemeName(name), description, thumbnail);
    }

    public static Theme restore(final String id, final String name, final String description, final String thumbnail) {
        return new Theme(Id.create(id), new ThemeName(name), description, thumbnail);
    }

    public String id() {
        return id.value();
    }

    public String name() {
        return name.value();
    }

    public String description() {
        return description;
    }

    public String thumbnail() {
        return thumbnail;
    }

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof final Theme theme)) return false;

        return Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
