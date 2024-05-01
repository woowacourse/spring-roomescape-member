package roomescape.domain.theme;

import java.util.Objects;

public class ThemeName {

    private final String value;

    private ThemeName(String value) {
        this.value = value;
    }

    public static ThemeName from(String value) {
        return new ThemeName(value);
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
