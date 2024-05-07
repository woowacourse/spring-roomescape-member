package roomescape.domain;

import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class ThemeName {
    private static final int MINIMUM_NAME_LENGTH = 1;
    private static final int MAXIMUM_NAME_LENGTH = 10;
    private static final String INVALID_NAME_LENGTH = String.format("이름은 %d자 이상, %d자 이하여야 합니다.", MINIMUM_NAME_LENGTH,
            MAXIMUM_NAME_LENGTH);

    private final String value;

    public ThemeName(String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(final String name) {
        if (name.isEmpty() || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new InvalidReservationException(INVALID_NAME_LENGTH);
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
        ThemeName themeName = (ThemeName) o;
        return Objects.equals(value, themeName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
