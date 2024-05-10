package roomescape.domain.theme;

import java.util.Objects;

public class ThemeDescription {

    private final String value;

    private ThemeDescription(String value) {
        this.value = value;
    }

    public static ThemeDescription from(String value) {
        validateNull(value);
        return new ThemeDescription(value);
    }

    private static void validateNull(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 비어있을 수 없습니다.");
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
        ThemeDescription other = (ThemeDescription) o;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "ThemeDescription{" +
                "value='" + value + '\'' +
                '}';
    }
}
