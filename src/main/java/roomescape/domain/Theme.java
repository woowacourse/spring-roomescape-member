package roomescape.domain;

import roomescape.domain.vo.Description;
import roomescape.domain.vo.Name;
import roomescape.domain.vo.ThumbnailUrl;

import java.util.Objects;

public class Theme {
    private final Long id;
    private final Name name;
    private final ThumbnailUrl thumbnailUrl;
    private final Description description;

    public Theme(Long id, Name name, ThumbnailUrl thumbnailUrl, Description description) {
        validateNotNull(name, thumbnailUrl, description);
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
    }

    public static Theme create(Name name, ThumbnailUrl thumbnailUrl, Description description) {
        return new Theme(null, name, thumbnailUrl, description);
    }

    private static void validateNotNull(Name name, ThumbnailUrl thumbnailUrl, Description description) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(thumbnailUrl);
        Objects.requireNonNull(description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Theme theme)) {
            return false;
        }

        return Objects.equals(id, theme.id);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public ThumbnailUrl getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Description getDescription() {
        return description;
    }
}
