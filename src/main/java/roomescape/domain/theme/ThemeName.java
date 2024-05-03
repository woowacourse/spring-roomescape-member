package roomescape.domain.theme;

import java.util.Objects;
import roomescape.exception.InvalidValueException;

public class ThemeName {

    private final String value;

    private ThemeName(String value) {
        this.value = value;
    }

    public static ThemeName from(String value) {
        validateNull(value);
        return new ThemeName(value);
    }

    private static void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidValueException("테마명은 공백일 수 없습니다.");
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
        ThemeName other = (ThemeName) o;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "ThemeName{" +
                "value='" + value + '\'' +
                '}';
    }
}
