package roomescape.domain;

import common.exception.ErrorCode;
import common.exception.RoomEscapeException;
import java.util.Objects;

public class Name {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 20;

    private final String value;

    private Name(String value) {
        this.value = value;
    }

    public static Name from(String value) {
        validateIsNull(value);
        String preprocessed = value.strip();
        validateLength(preprocessed);

        return new Name(preprocessed);
    }

    private static void validateIsNull(String value) {
        if (value == null) {
            throw new RoomEscapeException(ErrorCode.NAME_MUST_NOT_BE_NULL);
        }
    }

    public static void validateLength(String value) {
        if (value.length() < MIN_NAME_LENGTH || value.length() > MAX_NAME_LENGTH) {
            throw new RoomEscapeException(ErrorCode.INVALID_NAME_LENGTH);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
