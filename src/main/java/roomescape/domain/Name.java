package roomescape.domain;

import roomescape.exception.InvalidInputException;

public record Name(String value) {

    public Name {
        validate(value);
    }

    private void validate(final String name) {
        if (name.isBlank()) {
            throw InvalidInputException.of("name", name);
        }
    }
}
