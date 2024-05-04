package roomescape.domain;

import roomescape.domain.exception.Validate;

public record Theme(Long id, String name, String description, String thumbnail) {
    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme {
        Validate.AllNonNull(name, description, thumbnail);
    }

    public Theme createWithId(Long id) {
        return new Theme(id, this.name, this.description, this.thumbnail);
    }
}
