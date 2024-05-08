package roomescape.domain;

import roomescape.exception.InvalidInputException;

public record Limit(int value) {

    public Limit {
        validatePositive(value);
    }

    private void validatePositive(final int value) {
        if (value <= 0) {
            throw InvalidInputException.of("limit", value);
        }
    }

    public static Limit from(final int value) {
        return new Limit(value);
    }

}
