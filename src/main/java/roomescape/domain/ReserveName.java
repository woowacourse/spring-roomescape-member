package roomescape.domain;

import roomescape.domain.exception.InvalidNameException;

public record ReserveName(String value) {

    private static final int MAX_LENGTH = 100;

    public ReserveName {
        validateBlank(value);
        validateLength(value);
    }

    private void validateBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidNameException("Name cannot be null or empty");
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new InvalidNameException("Name cannot exceed " + value + " characters");
        }
    }
}
