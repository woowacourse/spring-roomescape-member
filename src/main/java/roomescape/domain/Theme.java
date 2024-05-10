package roomescape.domain;

import roomescape.domain.util.Validator;

public record Theme(Long id, String name, String description, String thumbnail) {
    private static final int MAX_STRING_LENGTH = 255;

    public Theme(String name, String description, String thumbnail) {
        this(null, name, description, thumbnail);
    }

    public Theme {
        Validator.nonNull(name, description, thumbnail);
        Validator.notEmpty(name, thumbnail);
        Validator.overSize(MAX_STRING_LENGTH, name, description, thumbnail);
    }

    public Theme createWithId(Long id) {
        return new Theme(id, name, description, thumbnail);
    }
}
