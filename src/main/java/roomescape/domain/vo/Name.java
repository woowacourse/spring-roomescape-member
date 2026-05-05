package roomescape.domain.vo;

import java.util.Objects;

public class Name {
    private static final int MAX_LENGTH = 15;
    private static final int MIN_LENGTH = 2;
    private final String value;

    public Name(String value) {
        validateLength(value);
        this.value = value;
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Name name1)) {
            return false;
        }

        return Objects.equals(value, name1.value);
    }

    @Override
    public String toString() {
        return value;
    }
}

