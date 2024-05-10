package roomescape.domain;

import roomescape.exception.InvalidInputException;

public record Name(String name) {

    public Name {
        validate(name);
    }

    private void validate(final String name) {
        if (name.isBlank()) {
            throw InvalidInputException.of("name", name);
        }
    }

    public String asString() {
        return name;
    }
}
